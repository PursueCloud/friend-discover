package com.yo.friendis.common.common.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yo.friendis.common.common.util.StringUtil;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author yhl
 *
 * @param <T>
 */
public class AbstractService<T> implements BaseService<T> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	protected Mapper<T> baseMapper;

	private Class<?> clazz = null;

	/**
	 * 实体类的类
	 * 
	 * @return
	 */
	public Class<?> getEntityClass() {
		if (clazz == null) {
			clazz = (Class<?>) ((ParameterizedType) getClass() // Class字节码
					.getGenericSuperclass()) // 因为对于T.class我们无法获取，但是这个方法就能获取到父类的参数类型，返回值为ParameterizedType
							.getActualTypeArguments()[0]; // 数组里第一个就是子类继承父类时所用类型
		}
		return clazz;
	}

	public int add(T entity) {
		return baseMapper.insert(entity);
	}

	public int addSelective(T entity) {
		return baseMapper.insertSelective(entity);
	}

	public int update(T entity) {
		return baseMapper.updateByPrimaryKey(entity);
	}

	public int updateByPrimaryKeySelective(T entity) {
		return baseMapper.updateByPrimaryKeySelective(entity);
	}

	public int delete(T entity) {
		return baseMapper.deleteByPrimaryKey(entity);
	}

	public int deleteByPrimaryKey(Object primaryKey) {
		return baseMapper.deleteByPrimaryKey(primaryKey);
	}

	@Override
	public int deleteByPrimaryKeys(List<String> primaryKeys) {
		int ret = 0;
		for (String key : primaryKeys) {
			ret += baseMapper.deleteByPrimaryKey(key);
		}
		return ret;
	}

	@Override
	@Deprecated
	public int deleteByField(Class<?> claszz, String field, String value) {
		return deleteByFields(claszz, new String[] { field }, new String[] { value });
	}

	@Override
	public int deleteByField(String field, String value) {
		return deleteByFields(new String[] { field }, new String[] { value });
	}

	@Override
	@Deprecated
	public int deleteByFields(Class<?> claszz, String[] fields, String[] values) {
		Example example = new Example(claszz);
		if (fields == null || fields.length == 0 || values == null || fields.length != values.length) {
			// 如果参数为空或者不正确，则不执行任何动作。
			return 0;
		}
		for (int i = 0; i < fields.length; i++) {
			example.createCriteria().andEqualTo(fields[i], values[i]);
		}
		return deleteByExample(example);
	}

	public int deleteByExample(Example example) {
		return baseMapper.deleteByExample(example);
	}

	@Override
	public int deleteByFields(String[] fields, String[] values) {
		Example example = new Example(getEntityClass());
		if (fields == null || fields.length == 0 || values == null || fields.length != values.length) {
			// 如果参数为空或者不正确，则不执行任何动作。
			return 0;
		}
		for (int i = 0; i < fields.length; i++) {
			example.createCriteria().andEqualTo(fields[i], values[i]);
		}
		return deleteByExample(example);
	}

	public T selectOne(T entity) {
		return baseMapper.selectOne(entity);
	}

	public T selectByPrimaryKey(Object primaryKey) {
		return baseMapper.selectByPrimaryKey(primaryKey);
	}

	@Override
	public T selectByEntity(T entity) {
		return baseMapper.selectByPrimaryKey(entity);
	}

	/**
	 * 单表分页查询
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<T> selectPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return baseMapper.selectAll();
	}

	@Deprecated
	public List<T> selectByField(Class<?> modelClazz, String field, String value, int pageNum, int pageSize) {
		return selectByFields(modelClazz, new String[] { field }, value, pageNum, pageSize);
	}

	public List<T> selectByField(String field, String value, int pageNum, int pageSize) {
		return selectByFields(new String[] { field }, value, pageNum, pageSize);
	}

	@Deprecated
	public List<T> selectAllByField(String value, Class<?> modelClazz, String field) {
		return selectByField(modelClazz, field, value, 0, 0);
	}

	public List<T> selectAllByField(String value, String field) {
		return selectByField(field, value, 0, 0);
	}

	@Override
	@Deprecated
	public List<T> selectByFields(Class<?> modelClazz, String[] fields, String keyword, int pageNum, int pageSize) {
		Example example = new Example(modelClazz);
		if (!StringUtils.isEmpty(keyword)) {
			keyword = StringUtil.keywords(keyword);
			for (String field : fields) {
				example.or().andLike(field, keyword);
			}
		}
		PageHelper.startPage(pageNum, pageSize);
		return baseMapper.selectByExample(example);
	}

	@Override
	public List<T> selectByFields(String[] fields, String keyword, int pageNum, int pageSize) {
		Example example = new Example(getEntityClass());
		if (!StringUtils.isEmpty(keyword)) {
			keyword = StringUtil.keywords(keyword);
			for (String field : fields) {
				example.or().andLike(field, keyword);
			}
		}
		PageHelper.startPage(pageNum, pageSize);
		return baseMapper.selectByExample(example);
	}

	@Override
	@Deprecated
	public List<T> selectByEqField(Class<?> clazz, String field, String value, int pageNum, int pageSize) {
		return selectByEqFields(clazz, new String[] { field }, value, pageNum, pageSize);
	}

	@Override
	public List<T> selectByEqField(String field, String value, int pageNum, int pageSize) {
		return selectByEqFields(new String[] { field }, new String[] { value }, pageNum, pageSize);
	}

	@Override
	public T selectOneByEqField(String field, String value) {
		List<T> list = selectByEqField(field, value, 1, 1);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	@Deprecated
	public List<T> selectAllByEqField(Class<?> clazz, String field, String value) {
		return selectByEqField(clazz, field, value, 0, 0);
	}

	@Override
	public List<T> selectAllByEqField(String field, String value) {
		return selectByEqField(field, value, 0, 0);
	}

	/**
	 * 根据多个字段相等查询
	 * 
	 * @param keyword
	 * @param clazz
	 * @param fields
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Deprecated
	protected List<T> selectByEqFields(Class<?> clazz, String[] fields, String keyword, int pageNum, int pageSize) {
		Example example = new Example(clazz);
		if (!StringUtils.isEmpty(keyword)) {
			for (String field : fields) {
				example.createCriteria().andEqualTo(field, keyword);
			}
		}
		PageHelper.startPage(pageNum, pageSize);
		return baseMapper.selectByExample(example);
	}

	/**
	 * 根据多个字段相等查询,当值比fields少时，多出的字段不加入到相等条件中
	 * 
	 * @param fields
	 * @param values
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<T> selectByEqFields(String[] fields, String[] values, int pageNum, int pageSize) {
		Example example = new Example(getEntityClass());
		if (values != null && values.length > 0) {
			Criteria criteria = example.createCriteria();
			for (int i = 0; i < fields.length; i++) {
				if (i >= values.length) {
					break;
				}
				criteria.andEqualTo(fields[i], values[i]);
			}
		}
		PageHelper.startPage(pageNum, pageSize);
		return baseMapper.selectByExample(example);
	}

}
