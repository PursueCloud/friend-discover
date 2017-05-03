package com.yo.friendis.common.cache;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

public class SimpleCache implements Cache {
	private static final Object NULL_HOLDER = new NullHolder();

	private final String name;

	private final ConcurrentMap<Object, Object> store;

	private final boolean allowNullValues;

	/**
	 * Create a new ConcurrentMapCache with the specified name.
	 * 
	 * @param name
	 *            the name of the cache
	 */
	public SimpleCache(String name) {
		this(name, new ConcurrentHashMap<Object, Object>(256), true);
	}

	/**
	 * Create a new ConcurrentMapCache with the specified name.
	 * 
	 * @param name
	 *            the name of the cache
	 * @param allowNullValues
	 *            whether to accept and convert {@code null}
	 *            values for this cache
	 */
	public SimpleCache(String name, boolean allowNullValues) {
		this(name, new ConcurrentHashMap<Object, Object>(256), allowNullValues);
	}

	/**
	 * Create a new ConcurrentMapCache with the specified name and the
	 * given internal {@link ConcurrentMap} to use.
	 * 
	 * @param name
	 *            the name of the cache
	 * @param store
	 *            the ConcurrentMap to use as an internal store
	 * @param allowNullValues
	 *            whether to allow {@code null} values
	 *            (adapting them to an internal null holder value)
	 */
	public SimpleCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(store, "Store must not be null");
		this.name = name;
		this.store = store;
		this.allowNullValues = allowNullValues;
	}

	@Override
	public final String getName() {
		return this.name;
	}

	@Override
	public final ConcurrentMap<Object, Object> getNativeCache() {
		return this.store;
	}

	public final boolean isAllowNullValues() {
		return this.allowNullValues;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object value = this.store.get(key);
		return toWrapper(value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> type) {
		Object value = fromStoreValue(this.store.get(key));
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		if (this.store.containsKey(key)) {
			return (T) get(key).get();
		} else {
			synchronized (this.store) {
				if (this.store.containsKey(key)) {
					return (T) get(key).get();
				}
				T value;
				try {
					value = valueLoader.call();
				} catch (Exception ex) {
					throw new ValueRetrievalException(key, valueLoader, ex);
				}
				put(key, value);
				return value;
			}
		}
	}

	@Override
	public void put(Object key, Object value) {
		this.store.put(key, toStoreValue(value));
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Object existing = this.store.putIfAbsent(key, value);
		return toWrapper(existing);
	}

	/**
	 * 支持模糊匹配（a:b:*）删除缓存
	 * 
	 * @param key
	 */
	@Override
	public void evict(Object key) {
		if (key instanceof String) {
			if (StringUtils.endsWithIgnoreCase((String) key, "*")) {
				String keyword = StringUtils.substringBefore((String) key, "*");
				Set<Object> keys = keys(keyword);
				for (Object k : keys) {
					this.store.remove(k);
				}
				return;
			}
		}
		this.store.remove(key);
	}

	/**
	 * keys("a:b:*")={a:b:1,a:b:2}
	 * 
	 * @param keyword
	 * @return
	 */
	public Set<Object> keys(Object keyword) {
		Set<Object> keys = new HashSet<Object>();
		if (keyword instanceof String) {
			String keywordStr = StringUtils.trim((String) keyword);
			if (StringUtils.isEmpty(keywordStr) || StringUtils.equals("*", keywordStr)) {
				return this.store.keySet();
			}
			keywordStr = StringUtils.substringBefore(keywordStr, "*");

			for (Object key : this.store.keySet()) {
				String keyStr = (String) key;
				if (StringUtils.startsWith(keyStr, keywordStr)) {
					keys.add(keyStr);
				}
			}
		}

		for (Object key : this.store.keySet()) {
			if (key.equals(keyword)) {
				keys.add(key);
			}
		}
		return keys;

	}

	@Override
	public void clear() {
		this.store.clear();
	}

	/**
	 * Convert the given value from the internal store to a user value
	 * returned from the get method (adapting {@code null}).
	 * 
	 * @param storeValue
	 *            the store value
	 * @return the value to return to the user
	 */
	protected Object fromStoreValue(Object storeValue) {
		if (this.allowNullValues && storeValue == NULL_HOLDER) {
			return null;
		}
		return storeValue;
	}

	/**
	 * Convert the given user value, as passed into the put method,
	 * to a value in the internal store (adapting {@code null}).
	 * 
	 * @param userValue
	 *            the given user value
	 * @return the value to store
	 */
	protected Object toStoreValue(Object userValue) {
		if (this.allowNullValues && userValue == null) {
			return NULL_HOLDER;
		}
		return userValue;
	}

	private ValueWrapper toWrapper(Object value) {
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}

	@SuppressWarnings("serial")
	private static class NullHolder implements Serializable {
	}

}
