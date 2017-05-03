package com.yo.friendis.common.cache;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

public class SimpleCacheManager extends AbstractCacheManager {

	private Collection<? extends Cache> caches;
	private boolean dynamic = true;
	private boolean allowNullValues = true;

	/**
	 * Specify the collection of Cache instances to use for this CacheManager.
	 */
	public void setCaches(Collection<? extends Cache> caches) {
		this.caches = caches;
	}

	@Override
	public void afterPropertiesSet() {
		if (this.caches == null) {
			return;
		}
		super.afterPropertiesSet();
	}

	@Override
	protected Collection<? extends Cache> loadCaches() {
		return this.caches;
	}

	@Override
	protected Cache getMissingCache(String name) {
		if (!dynamic) {
			return null;
		}
		Cache cache = new SimpleCache(name, this.allowNullValues);
		addCache(cache);
		return getCache(name);
	}

}
