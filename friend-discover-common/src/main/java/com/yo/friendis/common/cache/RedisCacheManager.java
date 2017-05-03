package com.yo.friendis.common.cache;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.redis.core.RedisOperations;

public class RedisCacheManager extends org.springframework.data.redis.cache.RedisCacheManager {

	/**
	 * Construct a {@link RedisCacheManager}.
	 * 
	 * @param redisOperations
	 */
	@SuppressWarnings("rawtypes")
	public RedisCacheManager(RedisOperations redisOperations) {
		this(redisOperations, Collections.<String> emptyList());
	}

	/**
	 * Construct a static {@link RedisCacheManager}, managing caches for the specified cache names only.
	 * 
	 * @param redisOperations
	 * @param cacheNames
	 * @since 1.2
	 */
	@SuppressWarnings("rawtypes")
	public RedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
		super(redisOperations, cacheNames);
	}

	@Override
	protected RedisCache createCache(String cacheName) {
		long expiration = computeExpiration(cacheName);
		return new RedisCache(cacheName, (this.isUsePrefix() ? this.getCachePrefix().prefix(cacheName) : null), this.getRedisOperations(), expiration);
	}

}
