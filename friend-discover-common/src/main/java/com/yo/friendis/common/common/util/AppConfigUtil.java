package com.yo.friendis.common.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * properties文件 配置读取工具类
 * 
 * @author yhl
 *
 */
public class AppConfigUtil {
	protected final static HashMap<String, String> config = new HashMap<String, String>();
	Logger logger = LoggerFactory.getLogger(getClass());

	public void setProperties(Resource[] properties) {
		config.clear();
//		proerties加载顺序
//		1.加载web项目中appconfig.properties
//		2.加载jar中appconfig.properties
		for (Resource f : properties) {
			Properties p = new Properties();
			try {
				p.load(f.getInputStream());
				for (Object key : p.keySet()) {
					if (!config.containsKey(key)) {
						config.put(key.toString(), p.getProperty(key.toString()));
					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

	}

	public static String getConfig(String key) {
		return config.get(key);
	}

}
