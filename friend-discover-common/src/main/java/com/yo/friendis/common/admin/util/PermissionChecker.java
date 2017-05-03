package com.yo.friendis.common.admin.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.yo.friendis.common.common.util.ConfigServiceUtil;

public class PermissionChecker {
	/**
	 * 检查权限开关
	 * 
	 * @param p
	 * @return 权限关闭 true,权限开启为false
	 */
	public static boolean checkPermsOff(String p) {
		return ConfigServiceUtil.isOff(p);
	}

	public static boolean isPermitted(String permission) {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(permission);
	}

	public static boolean hasRole(String roleId) {
		Subject subject = SecurityUtils.getSubject();
		return subject.hasRole(roleId);
	}
}
