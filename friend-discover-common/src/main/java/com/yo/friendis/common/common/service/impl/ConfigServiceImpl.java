package com.yo.friendis.common.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yo.friendis.common.common.mapper.ConfigMapper;
import com.yo.friendis.common.common.model.Config;
import com.yo.friendis.common.common.service.AbstractService;
import com.yo.friendis.common.common.service.ConfigService;

import tk.mybatis.mapper.entity.Example;

@Service
public class ConfigServiceImpl extends AbstractService<Config> implements ConfigService {
	@Autowired
	private ConfigMapper configMapper;
	private static final String GLOBAL_USER_ID = "-1";

	public Config getConfig(String key, String userId) {
		Config config = new Config(key, null, userId);
		return configMapper.selectOne(config);
	}

	public Config getConfig(String key) {
		return getConfig(key, GLOBAL_USER_ID);
	}

	public String getConfigValue(String key) {
		Config config = configMapper.selectOne(new Config(key, GLOBAL_USER_ID));
		if (config != null) {
			return config.getValue();
		}
		return "";
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public Config update(String key, String value, String userId) {
		Config config = getConfig(key, userId);
		if (config == null) {
			config = new Config(key, value, userId);
			addSelective(config);
		} else {
			config.setValue(value);
			Example example = new Example(Config.class);
			example.createCriteria().andEqualTo(Config.FIELD_KEY, key).andEqualTo(Config.FIELD_USER_ID, userId);
			configMapper.updateByExampleSelective(config, example);
		}
		return config;
	}

	@Override
	public Config update(String key, String value) {
		return update(key, value, GLOBAL_USER_ID);
	}

}
