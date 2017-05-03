package com.yo.friendis.common.common.jdbc;

public class DBContextHolder {
	// 线程安全的ThreadLocal
	private static final ThreadLocal<String> CONTEXTHOLDER = new ThreadLocal<String>();

	public static void setDbType(String dbType) {
		CONTEXTHOLDER.set(dbType);
	}

	public static String getDbType() {
		return ((String) CONTEXTHOLDER.get());
	}

	public static void clearDbType() {
		CONTEXTHOLDER.remove();
	}

}
