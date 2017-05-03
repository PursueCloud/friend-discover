package com.yo.friendis.common.shiro.realms;

import java.util.List;

import com.yo.friendis.common.admin.model.AdminUser;
import com.yo.friendis.common.admin.service.AdminUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import com.yo.friendis.common.admin.service.AdminPermissionService;
import com.yo.friendis.common.admin.service.AdminRoleService;

public class Realm extends AuthorizingRealm {

	public final static String CACHE_NAME_1 = "com.yo.friendis.common.shiro.realms.Realm.authenticationCache";
	public final static String CACHE_NAME_2 = "com.yo.friendis.common.shiro.realms.Realm.authorizationCache";

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private AdminPermissionService adminPermissionService;

	public Realm() {
		setName(getClass().getName());
	}

	/*
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection) 获取用户的所有角色、所有的permission return 权限信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		AdminUser user = (AdminUser) principals.fromRealm(getName()).iterator().next();
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<String> roleIds = adminRoleService.getRoleIdsByUserId(user.getUserId());
			for (String roleId : roleIds) {
				info.addRole(roleId + "");
				info.addStringPermissions(adminPermissionService.getPermissionByRole(roleId));
			}
			return info;
		} else {
			return null;
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		AdminUser user = adminUserService.getUserByAccount(token.getUsername());
		if (user != null) {
			return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		} else {
			return null;
		}
	}

	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		AdminUser user = (AdminUser) principals.getPrimaryPrincipal();
		return "shiro.auth.User:" + user.getUserId();
	}

}
