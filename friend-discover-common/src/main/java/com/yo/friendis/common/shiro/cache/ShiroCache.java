package com.yo.friendis.common.shiro.cache;

import java.util.Collection;
import java.util.Set;

import com.yo.friendis.common.admin.model.AdminUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.cache.Cache.ValueWrapper;

public class ShiroCache<K, V> implements Cache<K, V> {
	private org.springframework.cache.Cache cache;

	public ShiroCache(org.springframework.cache.Cache cache) {
		this.cache = cache;
	}

	@Override
	public V get(K key) throws CacheException {
		ValueWrapper value = null;
		value = cache.get(key);
		if (value == null) {
			return null;
		}
		return (V) value.get();
	}

	@Override
	public V put(K key, V value) throws CacheException {
		cache.put(key, value);
		return value;
	}

	boolean isSimplePrincipalCollection(K key) {
		return key instanceof SimplePrincipalCollection;
	}

	String formatKey(K key) {
		SimplePrincipalCollection spc = (SimplePrincipalCollection) key;
		AdminUser user = (AdminUser) spc.getPrimaryPrincipal();
		return "User:" + user.getUserId();
	}

	@Override
	public V remove(K key) throws CacheException {
		V value = get(key);
		cache.evict(key);
		return value;
	}

	@Override
	public void clear() throws CacheException {
		cache.clear();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Set<K> keys() {
		return null;
	}

	@Override
	public Collection<V> values() {
		return null;
	}

}
