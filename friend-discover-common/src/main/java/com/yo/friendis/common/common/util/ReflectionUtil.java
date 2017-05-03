package com.yo.friendis.common.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

import org.springframework.util.Assert;


/**
 * 反射工具类
 * 
 * @author walidake
 *
 */
public class ReflectionUtil {

	/**
	 * 转换T1为T2
	 * 
	 * 注意：T1需全包含T2属性，或者T2需包含T1全部属性
	 * 			另外，反射影响性能，一般能不使用该方法就不使用
	 * 
	 * @param t1
	 * @param toClazz
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	public static <T1,T2> T2 convert(T1 t1,Class<?> toClazz) throws IllegalArgumentException, IllegalAccessException, InstantiationException{
		Assert.isTrue(t1 != null, "t1 must be specified");
		Assert.notNull(toClazz, "Class must not be null");
		
		//简单类型不接受反射
		//不使用instanceof 判断
		if(!Object.class.isAssignableFrom(toClazz)){
			return null;
		}
		
		T2 t2 = (T2) toClazz.newInstance();
		if(Object.class.isAssignableFrom(t1.getClass())){
			Field[] fields = getAllFields(t1.getClass());
			Field[] fields2 = getAllFields(toClazz);
			if(fields != null && fields2 != null){
				//选择实体属性少的进行反射
				fields = fields.length < fields2.length ? fields : fields2;
				
				for (Field field : fields) {
					if(field !=  null && isAccessible(field)){
						field.setAccessible(true);
						field.set(t2, field.get(t1));
					}
				}
			}
		}
		return t2;
	}
	
	
	/**
	 * 转换T1为T2
	 * 
	 * 注意：类型收敛，即不支持双向转换，只能由大类型向小类型转换（T1不包含T2属性，可通过map进行field转换）
	 * 			另外，反射影响性能，一般能不使用该方法就不使用
	 * 
	 * @param t1
	 * @param toClazz
	 * @param fieldAndFields
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@SuppressWarnings("unchecked")
	public static <T1,T2> T2 converge(T1 t1,Class<?> toClazz,Map<String, String> fieldAndFields) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException{
		Assert.isTrue(t1 != null, "t1 must be specified");
		Assert.notNull(toClazz, "Class must not be null");
		Assert.notNull(fieldAndFields, "fieldAndFields must not be null");
		
		//简单类型不接受反射
		//不使用instanceof 判断
		if(!Object.class.isAssignableFrom(toClazz)){
			return null;
		}
		
		T2 t2 = (T2) toClazz.newInstance();
		if(Object.class.isAssignableFrom(t1.getClass())){
			for(Map.Entry<String, String> fieldAndField : fieldAndFields.entrySet()){
				Field field = t1.getClass().getDeclaredField(fieldAndField.getKey());
				Field field2 = toClazz.getDeclaredField(fieldAndField.getValue());
				if(field !=  null && isAccessible(field) && field2 !=  null && isAccessible(field2)){
					field.setAccessible(true);
					field2.setAccessible(true);
					field2.set(t2, field.get(t1));
				}
			}
		}
		return t2;
	}
	
	
	/**
	 * 获取被注解属性的属性名
	 * 
	 * @param clazz
	 * @param annotationClass
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String getDeclaredFieldByAnnotationType(Class<?> clazz,Class<? extends Annotation> annotationClass) throws IllegalArgumentException, IllegalAccessException{
		Assert.isTrue(clazz != null, "clazz must be specified");
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			for (Field field : fields) {
				if(field !=  null && isAccessible(field) && field.isAnnotationPresent(annotationClass)){
					field.setAccessible(true);
					return field.getName();
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 获取被注解属性的值
	 * 
	 * @param t
	 * @param annotationClass
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> Object getDeclaredFieldValueByAnnotationType(T t,Class<? extends Annotation> annotationClass) throws IllegalArgumentException, IllegalAccessException{
		Assert.isTrue(t != null, "t must be specified");
		Field[] fields = t.getClass().getDeclaredFields();
		if (fields != null) {
			for (Field field : fields) {
				if(field !=  null && isAccessible(field) && field.isAnnotationPresent(annotationClass)){
					field.setAccessible(true);
					return field.get(t);
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取是否可访问
	 * 
	 * @param field
	 */
	public static boolean isAccessible(Field field) {
		return ((!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()))
				&& !Modifier.isFinal(field.getModifiers())
				&& !Modifier.isStatic(field.getModifiers())
				&& !field.isAccessible());
	}
	
	
	/**
	 * 获取类以及父类的属性集合
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllFields(Class<?> clazz){
		Field[] fields = clazz.getDeclaredFields();
		Class<?> superClazz = clazz.getSuperclass();
		if(fields != null && superClazz != Object.class){
			Field[] fields2 = superClazz.getDeclaredFields();
			if(fields2 != null && fields2.length > 0){
				int oldCapacity = fields.length;
				int capacity = fields.length + fields2.length;
				fields = Arrays.copyOf(fields, capacity);
				for (int i = oldCapacity,j = 0; i < capacity; i++,j++) {
					fields[i] = fields2[j];
				}
			}
		}
		return fields;
	}
	
	
	/**
	 * 设置属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void setFieldValue(Object obj, String fieldName,Object value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if(field !=  null && isAccessible(field)){
			field.setAccessible(true);
			field.set(obj, value);
		}
	}
	
	
	/**
	 * 获取属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if(field !=  null && isAccessible(field)){
			field.setAccessible(true);
			return field.get(obj);
		}
		return null;
	}
	
	
}
