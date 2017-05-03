package com.yo.friendis.common.common.service;

import com.yo.friendis.common.common.model.Config;

public interface ConfigService extends BaseService<Config> {
	static final String OFF = "0";
	static final String ON = "1";

	public Config getConfig(String key, String userId);

	public Config getConfig(String key);

	/**
	 * @param key
	 * @return ""或者值
	 */
	public String getConfigValue(String key);

	public Config update(String key, String value, String userId);

	public Config update(String key, String value);

}
