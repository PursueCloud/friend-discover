package com.yo.friendis.common.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminRole;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import com.yo.friendis.common.admin.model.AdminUserRole;
import org.springframework.beans.factory.annotation.Autowired;

import com.yo.friendis.common.admin.mapper.AdminRoleMapper;
import com.yo.friendis.common.admin.mapper.AdminRolePermissionMapper;
import com.yo.friendis.common.admin.mapper.AdminUserRoleMapper;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.service.AbstractService;

public class AdminRoleServiceImpl extends AbstractService<AdminRole> {
	@Autowired
	private AdminRoleMapper adminRoleMapper;

	/**
	 * 
	 */
	@Autowired
	private AdminUserRoleMapper adminUserRoleMapper;
	@Autowired
	private AdminRolePermissionMapper adminRolePermissionMapper;

	/**
	 * 获取角色管理表格数据
	 */
	public List<Map<String, Object>> getAdminRole(Map<String, Object> con) {
		return adminRoleMapper.getAdminRole(con);
	}

	public List<String> getRoleIdsByUserId(String userId) {
		List<String> ids = new ArrayList<String>();
		List<AdminUserRole> userRoles = adminUserRoleMapper.select(new AdminUserRole(userId, null));
		for (AdminUserRole ur : userRoles) {
			ids.add(ur.getRoleId());
		}
		return ids;
	}


	public int updateAdminRole(Map<String, Object> role) {
		return adminRoleMapper.updateAdminRole(role);
	}

	public void deleteAdminRole(List<String> ids) {
		adminRoleMapper.deleteAdminRole(ids);
	}

	/**
	 * 根据list获取权限对象数据
	 * 
	 * @param roleId
	 *            角色id
	 * @param name
	 *            权限对象
	 * @param primkeys
	 *            对象id集合
	 * @return
	 */
	public List<Resource> getResources(String roleId, String name, String[] primkeys) {
		Map<String, Object> con = new HashMap<String, Object>();
		con.put("roleId", roleId);
		con.put("name", name);
		con.put("primkeys", primkeys);
		return adminRoleMapper.getResources(con);
	}

	/**
	 * 更新角色权限
	 * 
	 * @param roleId
	 * @param action
	 *            动作：view、update、delete、add等
	 * @param name
	 *            权限对象，如Menu、TagIndex,拓展可以使用类名
	 * @param primkey
	 *            具体对象的id
	 * @param checked
	 *            是否勾选
	 * @return
	 */
	public OperaterResult<String> updateRolePermission(String roleId, String action, String name, String primkey, String checked) {
		int resInt = 0;
		resInt = adminRoleMapper.updateRolePermission(new AdminRolePermission(roleId, action, name, primkey));
		return new OperaterResult<String>(resInt > 0, "完成操作");
	}

	public int addRolePermission(AdminRolePermission rolePermission) {
		int resInt = 0;
		resInt = adminRoleMapper.addRolePermission(rolePermission);
		return resInt;
	}

	public int deleteRolePermission(String roleId, String name, String primkey) {
		int resInt = 0;
		resInt = adminRoleMapper.deleteRolePermission(roleId, name, primkey);
		return resInt;
	}

	@Override
	public int deleteByPrimaryKeys(List<String> primaryKeys) {
		int ret = 0;
		for (String primaryKey : primaryKeys) {
//			 删除角色-用户关系
			adminUserRoleMapper.delete(new AdminUserRole(null, primaryKey));
//			 删除角色-权限关系
			adminRolePermissionMapper.delete(new AdminRolePermission(primaryKey));
//			删除角色
			ret += adminRoleMapper.deleteByPrimaryKey(primaryKey);
		}
		return ret;
	}

	public int deleteUserRoleByRoleId(String roleId) {
		return adminUserRoleMapper.delete(new AdminUserRole(null, roleId));
	}

	public List<AdminRole> getRolesByUserId(String userId) {
		return adminRoleMapper.getRolesByUserId(userId);
	}

	/**
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 *            角色名关键词
	 * @return name含key的用户
	 */
	public List<AdminRole> searchRoles(String keyword, int pageNum, int pageSize) {
		return selectByField(AdminRole.class, keyword, keyword, pageNum, pageSize);
	}

	public List<AdminRole> searchByRoleName(String keyword, int pageNum, int pageSize) {
		return selectByFields(AdminRole.class, new String[] { AdminRole.FIELD_ROLE_NAME }, keyword, pageNum, pageSize);
	}

}
