package com.yo.friendis.common.common.util;

import com.yo.friendis.common.common.model.Config;
import com.yo.friendis.common.common.service.ConfigService;

/**
 * 数据库配置读取工具类
 * 
 * @author yhl
 *
 */
public class ConfigServiceUtil {
	private static ConfigService configService;

	public static ConfigService getService() {
		if (configService == null) {
			configService = BeanUtil.getBean(ConfigService.class);
		}
		return configService;
	}

	public static Config update(String key, String value) {
		return getService().update(key, value);
	}

	/**
	 * @param key
	 * @return ""或者值
	 */
	public static String getConfigValue(String key) {
		return getService().getConfigValue(key);
	}

	/**
	 * 检查开关
	 * 
	 * @param key
	 * @return 关闭 true,开启为false
	 */
	public static boolean isOff(String key) {
		return "0".equals(getConfigValue(key));// 关闭
	}

	/**
	 * 开
	 * 
	 * @param key
	 * @return 关闭 true,开启为false
	 */
	public static boolean turnOff(String key) {
		Config c = getService().update(key, ConfigService.OFF);
		return c != null;
	}

	/**
	 * 开
	 * 
	 * @param key
	 * @return 关闭 true,开启为false
	 */
	public static boolean turnOn(String key) {
		Config c = getService().update(key, ConfigService.ON);
		return c != null;
	}
}
