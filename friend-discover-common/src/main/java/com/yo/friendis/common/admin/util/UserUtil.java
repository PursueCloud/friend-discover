package com.yo.friendis.common.admin.util;

import com.yo.friendis.common.admin.model.AdminUser;
import com.yo.friendis.common.admin.service.AdminUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;

public class UserUtil {

	private static AdminUserService adminUserService;

	public UserUtil(AdminUserService adminUserService) {
		UserUtil.adminUserService = adminUserService;
	}

	/**
	 * 获取当前用户登录名
	 * 
	 * @return username
	 */
	public static String getCurrentUserName() {
		Subject subject = SecurityUtils.getSubject();
		AdminUser user = (AdminUser) subject.getPrincipal();
		return user == null ? null : user.getAccount();
	}

	/**
	 * 获取用户
	 * 
	 * @return User
	 */
	public static AdminUser getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		AdminUser user = (AdminUser) subject.getPrincipal();
		if (user == null) {
			user = new AdminUser();
			user.setAccount("guest");
			user.setUserName("游客");
		}
		return user;
	}

	public static String Sha256(String pwd) {
		String newpwd = new Sha256Hash(pwd).toHex();
		return newpwd;

	}

	public static boolean checkPassword(String account, String pwd) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, pwd);
		try {
			subject.login(token);
			return true;// 密码正确
		} catch (Exception e) {
			return false;
		}
	}
}
