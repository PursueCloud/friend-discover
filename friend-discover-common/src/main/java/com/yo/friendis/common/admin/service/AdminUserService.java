package com.yo.friendis.common.admin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminRole;
import com.yo.friendis.common.admin.model.AdminUser;
import com.yo.friendis.common.common.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yo.friendis.common.admin.mapper.AdminUserMapper;
import com.yo.friendis.common.common.service.AbstractService;

@Service(AdminUserService.BEAN_ID)
public class AdminUserService extends AbstractService<AdminUser> {
	public final static String BEAN_ID = "adminUserService";

	private final static String DEFAULT_PWD = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
	private final static int DEFAULT_STATUS = 1;
	private final static String DEFAULT_ROLE = "普通用户";

	@Autowired
	private AdminUserMapper adminUserMapper;
	@Autowired
	AdminRoleService adminRoleService;

	public String getName() {
		return getClass().getName();
	}

	public List<AdminUser> getUsers() {
		List<AdminUser> list = adminUserMapper.getUsers();
		return list;
	}

	public AdminUser getUserByUserId(String userId) {
		return adminUserMapper.getUserByUserId(userId);
	}

	public AdminUser getUserByAccount(String account) {
		if ( StringUtils.isBlank(account) ) {
			return null;
		}
		AdminUser user = new AdminUser();
		user.setAccount(account);
		return selectOne(user);
	}

	public List<AdminUser> getUsersByRole(@Param("roleId") String roleId) {
		return adminUserMapper.getUsersByRole(roleId);
	}

	public AdminUser getUser(String userId) {
		AdminUser user = adminUserMapper.getUserByUserId(userId);
		return user;
	}

	public AdminUser getUserByUserName(String userName) {
		return adminUserMapper.getUserByUserName(userName);
	}

	public String getUserIdByName(String userName) {
		String ret = adminUserMapper.getUserIdByName(userName);
		return ret;
	}

	public int countAdminUser() {
		return adminUserMapper.countAdminUser();
	}

	/**
	 * @param nameParam
	 *            包含userName\screenName
	 * @return
	 */
	public List<AdminUser> getAdminUser(Map<String, Object> nameParam) {
		return adminUserMapper.getAdminUser(nameParam);
	}

	public int addUserRole(String roleId, String userId) {
		return adminUserMapper.addUserRole(roleId, userId);
	}

	/**
	 * 移除用户的角色
	 * 
	 * @param userId
	 * @return
	 */
	public int deleteUserRoleByUserId(String userId) {
		return adminUserMapper.deleteUserRoleByUserId(userId);
	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param newPas
	 * @return
	 */
	public int editRolePas(String userId, String newPas) {
		return adminUserMapper.editRolePas(userId, newPas);
	}


	/**
	 * 查询没有该角色的用户
	 * 
	 * @param roleId
	 * @return
	 */
	public List<AdminUser> getUnSelUsersByRoleId(String roleId) {
		return adminUserMapper.getUnSelUsersByRoleId(roleId);
	}

	/**
	 * 查询有该角色的用户
	 * 
	 * @param roleId
	 * @return
	 */
	public List<AdminUser> getUsersByRoleId(String roleId) {
		return adminUserMapper.getUsersByRoleId(roleId);
	}

	/**
	 * 获取userName为Keyword的User
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<AdminUser> getUsersAndLike(String keyword, int pageNum, int pageSize) {
		return selectByFields(new String[] { AdminUser.FIELD_USER_NAME }, keyword, pageNum, pageSize);
	}

	/**
	 * 获取不是该RoleId而且Name为Keyword的User
	 * 
	 * @param keyword
	 * @param roleId
	 * @return
	 */
	public List<AdminUser> getUnSelUsersByRoleIdAndLike(String keyword, String roleId) {
		return adminUserMapper.getUnSelUsersByRoleIdAndLike(keyword, roleId);
	}

	/**
	 * 查询其他机构的用户
	 * 
	 * @param keyword
	 * @param roleId
	 * @return
	 */
	public List<AdminUser> getUsersByRoleIdAndLike(String keyword, String roleId) {
		return adminUserMapper.getUsersByRoleIdAndLike(keyword, roleId);
	}

	@Transactional
	public AdminUser save(AdminUser user, List<String> roleIds) {
		int ret = 0;
		if ( StringUtils.isBlank(user.getUserId()) ) {
			user.setPassword(DEFAULT_PWD);
			user.setStatus(DEFAULT_STATUS);
			user.setCreateTime(DateUtils.getCurrentTimestamp());
			user.setUserId("0");
			ret=this.addSelective(user);
			user = this.getUserByAccount(user.getAccount());
		} else {
			// 根据主键更新不为null的属性
			ret = this.updateByPrimaryKeySelective(user);
			this.deleteUserRoleByUserId(user.getUserId());
		}
		if (roleIds != null && !roleIds.isEmpty()) {
			for (String roleId : roleIds) {
				this.addUserRole(roleId, user.getUserId());
			}
		}
		return ret > 0 ? user : null;
	}

	public AdminUser addUserWithDefaultRole(String userName, String pwd) {
		int ret = 0;
		AdminUser user = new AdminUser();
		user.setAccount(userName);
		user.setUserName(userName);
		user.setPassword(pwd);
		user.setStatus(DEFAULT_STATUS);
		user.setCreateTime(DateUtils.getCurrentTimestamp());
		user.setUserId("0");
		ret = this.addSelective(user);
		this.addRoleWithEveryone(user.getUserId());
		return ret > 0 ? user : null;
	}

	/**
	 * 为所有用户添加默认角色
	 * 
	 * @param userId
	 */
	private void addRoleWithEveryone(String userId) {
		if ( StringUtils.isBlank(userId) ) {
			logger.warn("由于用户id为空，添加默认角色不成功。");
			return;
		}
		AdminRole role = adminRoleService.selectOneByEqField(AdminRole.FIELD_ROLE_NAME, DEFAULT_ROLE);
		if (role == null) {
			role = new AdminRole();
			role.setRoleName(DEFAULT_ROLE);
			role.setDescription("所有用户默认角色");
			role.setCreateTime(DateUtils.getCurrentTimestamp());
			adminRoleService.addSelective(role);
		}
		if ( StringUtils.isNotBlank(role.getRoleId()) ) {
			this.addUserRole(role.getRoleId(), userId);
		}
	}
}
