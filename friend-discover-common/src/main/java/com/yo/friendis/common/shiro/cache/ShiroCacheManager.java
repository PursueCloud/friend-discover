package com.yo.friendis.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class ShiroCacheManager implements CacheManager {
	public final static String CACHE_MANAGER = "shiroCacheManager";
	private org.springframework.cache.CacheManager cacheManager;

	public ShiroCacheManager(org.springframework.cache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		org.springframework.cache.Cache cache = cacheManager.getCache(name);
		return new ShiroCache<K, V>(cache);
	}

}
