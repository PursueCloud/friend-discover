package com.yo.friendis.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroSessionManager implements CacheManager {
	public final static String CACHE_MANAGER="shiroCacheManager";
	@Autowired
	private org.springframework.cache.CacheManager cacheManager;

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		org.springframework.cache.Cache cache = cacheManager.getCache(name);
		return new ShiroCache<K, V>(cache);
	}

}
