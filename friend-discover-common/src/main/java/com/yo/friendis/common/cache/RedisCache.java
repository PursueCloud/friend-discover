/*
 * Copyright 2011-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yo.friendis.common.cache;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisOperations;

/**
 * Cache implementation on top of Redis.
 *
 * @author hailong yan
 */
public class RedisCache extends org.springframework.data.redis.cache.RedisCache {
	@SuppressWarnings("rawtypes") //
	private final RedisOperations redisOperations;

	/**
	 * Constructs a new <code>RedisCache</code> instance.
	 *
	 * @param name
	 *            cache name
	 * @param prefix
	 * @param redisOperations
	 * @param expiration
	 */
	public RedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration) {
		super(name, prefix, redisOperations, expiration);
		this.redisOperations = redisOperations;

	}

	@Override
	public void evict(Object key) {
		if (key instanceof String) {
			if (StringUtils.endsWithIgnoreCase((String) key, "*")) {
				@SuppressWarnings("unchecked")
				Set<Object> keys = this.redisOperations.keys(key);
				for (Object k : keys) {
					super.evict(k);
				}
				return;
			}
		}
		super.evict(key);
	}
}
