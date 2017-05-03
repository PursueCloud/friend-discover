package com.yo.friendis.common.common.util;

import org.apache.commons.lang.StringUtils;
import java.io.IOException;
import java.util.Properties;

/**
 * Properties文件管理类
 * @author jay.huang
 *
 */
public class PropertyMgr {
	/**
	 * 数据库配置文件
	 */
	public static final String DB_CONFIG_PROP = "db-config.properties";
	/**
	 * 分类模型配置文件，用于配置模型python脚本路径，调用python的shell脚本路径，远程服务器ip和用户名密码等
	 */
	public static final String PS_CONFIG_PROP = "psconfig.properties";
	/**
	 * 关键字挖掘和整合优化配置文件，用于配置关键字挖掘和整合优化程序路径，调用R或python的shell脚本路径，远程服务器ip和用户名密码等
	 */
	public static final String MO_CONFIG_PROP = "moconfig.properties";
	
	public static final String CALL_PY_SH_URL_KEY = "callPyShUrl";
	public static final String MINING_PRE_MODEL_PY_URL_KEY = "miningPreModelPyUrl";
	public static final String MINING_SELF_LEARN_MODEL_PY_URL_KEY = "miningSelfLearnModelPyUrl";
	public static final String PRE_MODEL_PY_URL_KEY = "preModelPyUrl";
	public static final String SELF_LEARN_MODEL_PY_URL_KEY = "selfLearnModelPyUrl";
	public static final String RUN_MODEL_TYPE_KEY = "runModelType";
	public static final String USE_MINING_PROGRAM_TYPE_KEY = "useMiningProgramType";
	public static final String MINING_PY_URL_KEY = "miningPyUrl";
	public static final String MINING_R_URL_KEY = "miningRUrl";
	public static final String RUN_MINING_TYPE_KEY = "runMiningType";
	public static final String OPTIMIZE_RES_R_URL_KEY = "optimizeResRUrl";
	public static final String OPTIMIZE_COMBINE_R_URL_KEY = "optimizeCombineRUrl";
	public static final String RUN_OPTIMIZE_TYPE_KEY = "runOptimizeType";
	public static final String CALL_R_SH_URL_KEY = "callRShUrl";
	public static final String IP_KEY = "ip";
	public static final String USERNAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";
	public static final String BACKUP_DB_USERNAME_KEY = "backup.db.username";
	public static final String BACKUP_DB_PASSWORD_KEY = "backup.db.password";
	public static final String BACKUP_DB_LOCATION = "backup.db.location";

	private static Properties prop = null;
	
	private PropertyMgr() {
		
	}
	
	/**
	 * 根据配置文件、key值获取value
	 * @param propFileName
	 * @param key
	 * @return
	 */
	public static String getPropertyByKey(String propFileName, String key) {
		if(StringUtils.isBlank(key) || StringUtils.isBlank(propFileName)) {
			System.out.println("key值或配置文件名不能为空！");
			return null;
		}
		prop = new Properties();
		try {
			prop.load(PropertyMgr.class.getClassLoader().getResourceAsStream(propFileName));
		} catch (IOException e) {//如果找不到那个文件
			System.out.println("找不到properties文件：" + propFileName);
			e.printStackTrace();
			return null;
		}
		String value = prop.getProperty(key);
		return value;
	}
	
}
