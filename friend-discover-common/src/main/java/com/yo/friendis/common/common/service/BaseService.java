package com.yo.friendis.common.common.service;

import java.util.List;

public interface BaseService<T> {

	/**
	 * 实体新增
	 * 
	 * @param entity
	 * @return
	 */
	int add(T entity);

	/**
	 * 保存一个实体，null的属性不会保存，会使用数据库默认值
	 * 
	 * @param entity
	 * @return
	 */
	int addSelective(T entity);

	/**
	 * 根据主键更新所有属性
	 * 
	 * @param entity
	 * @return
	 */
	int update(T entity);

	/**
	 * 根据主键更新不为null的属性
	 * 
	 * @param entity
	 * @return
	 */
	int updateByPrimaryKeySelective(T entity);

	/**
	 * 根据主键删除
	 * 
	 * @param entity
	 * @return
	 */
	int delete(T entity);

	/**
	 * 根据主键删除
	 * 
	 * @param primaryKey
	 * @return
	 */
	int deleteByPrimaryKey(Object primaryKey);

	/**
	 * 根据主键批量删除
	 * 
	 * @param primaryKeys
	 * @return
	 */
	int deleteByPrimaryKeys(List<String> primaryKeys);

	/**
	 * 根据多个字段相等删除记录，条件为且
	 * 
	 * @param claszz
	 * @param fields
	 * @param values
	 * @return
	 */
	@Deprecated
	int deleteByFields(Class<?> claszz, String[] fields, String[] values);

	/**
	 * 根据多个字段相等删除记录，条件为且
	 * 
	 * @param fields
	 * @param values
	 * @return
	 */
	int deleteByFields(String[] fields, String[] values);

	/**
	 * 根据单个字段相等删除记录
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	@Deprecated
	int deleteByField(Class<?> claszz, String field, String value);

	/**
	 * 根据单个字段相等删除记录
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	int deleteByField(String field, String value);

	/**
	 * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
	 * 
	 * @param entity
	 * @return
	 */
	T selectOne(T entity);

	/**
	 * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
	 * 
	 * @param primaryKey
	 * @return
	 */
	T selectByPrimaryKey(Object primaryKey);

	/**
	 * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
	 * 
	 * @param entity
	 * @return
	 */
	T selectByEntity(T entity);

	/**
	 * 单表分页查询
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<T> selectPage(int pageNum, int pageSize);

	/**
	 * 根据多个字段名模糊查询
	 * 
	 * @param keyword
	 * @param clazz
	 * @param fields
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Deprecated
	List<T> selectByFields(Class<?> clazz, String[] fields, String keyword, int pageNum, int pageSize);

	/**
	 * 根据多个字段名模糊查询
	 * 
	 * @param keyword
	 * @param fields
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<T> selectByFields(String[] fields, String keyword, int pageNum, int pageSize);

	/**
	 * 根据单个字段名模糊查询
	 * 
	 * @param value
	 * @param clazz
	 * @param field
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Deprecated
	List<T> selectByField(Class<?> clazz, String field, String value, int pageNum, int pageSize);

	/**
	 * 根据单个字段名模糊查询 
	 * <pre>
	 * selectByField(String field, String value,0, 0) return all
	 * </pre> 
	 * @param value
	 * @param field
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<T> selectByField(String field, String value, int pageNum, int pageSize);

	/**
	 * 根据单个字段相等查询
	 * 
	 * <pre>
	 * selectByEqField(clazz,String field, String value,0, 0) return all
	 * </pre>
	 * 
	 * @param value
	 * @param clazz
	 * @param field
	 * @return
	 */
	@Deprecated
	List<T> selectByEqField(Class<?> clazz, String field, String value, int pageNum, int pageSize);

	/**
	 * <p>
	 * 根据单个字段相等查询
	 * </p>
	 * 
	 * <pre>
	 * selectByField(String field, String value,0, 0) return all
	 * </pre>
	 * 
	 * @param value
	 * @param field
	 * @return
	 */
	List<T> selectByEqField(String field, String value, int pageNum, int pageSize);

	/**
	 * 根据单个字段相等查询
	 * 
	 * @param value
	 * @param field
	 * @return
	 */
	T selectOneByEqField(String field, String value);

	/**
	 * 根据单个字段相等查询
	 * 
	 * @param value
	 * @param clazz
	 * @param field
	 * @return
	 */
	@Deprecated
	List<T> selectAllByEqField(Class<?> clazz, String field, String value);

	/**
	 * 根据单个字段相等查询
	 * 
	 * @param value
	 * @param field
	 * @return
	 */
	List<T> selectAllByEqField(String field, String value);
}
