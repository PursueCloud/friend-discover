package com.yo.friendis.common.common;

public interface Common {
	public final static String DB_PREFIX = "yo_";

	public enum DBConfigKeys {
		/**
		 * 机构根目录ID
		 */
		ORGANIZATION_ROOT,
		/**
		 * 文件服务地址
		 */
		FILE_SERVER_URL
	}
}
