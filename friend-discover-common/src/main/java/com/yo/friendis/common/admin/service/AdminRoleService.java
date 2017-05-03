package com.yo.friendis.common.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminRole;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import com.yo.friendis.common.admin.model.AdminUserRole;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yo.friendis.common.admin.mapper.AdminRoleMapper;
import com.yo.friendis.common.admin.mapper.AdminUserRoleMapper;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.admin.model.view.VRoleUser;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.service.AbstractService;
import com.yo.friendis.common.common.util.StringUtil;
import com.github.pagehelper.PageHelper;

@Service("roleService")
public class AdminRoleService extends AbstractService<AdminRole> {
	@Autowired
	private AdminRoleMapper adminRoleMapper;

	/**
	 * 
	 */
	@Autowired
	private AdminUserRoleMapper adminUserRoleMapper;

	/**
	 * 根据list获取权限对象数据
	 * 
	 * @param roleId
	 *            角色id
	 * @param name
	 *            权限类型
	 * @param primkeys
	 *            权限具体名称
	 * @return
	 */
	public List<Resource> getResources(String roleId, String name, String[] primkeys) {
		Map<String, Object> con = new HashMap<String, Object>();
		con.put("roleId", roleId);
		con.put("name", name);
		con.put("primkeys", primkeys);
		return adminRoleMapper.getResources(con);
	};

	/**
	 * 更新角色权限
	 * 
	 * @param roleId
	 *            角色id
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
	};

	/**
	 * 根据Primkey删除角色权限，Primkey为权限类型
	 * 
	 * @param roleId
	 * @param name
	 * @param primkey
	 * @return
	 */
	public int deleteRolePermission(String roleId, String name, String primkey) {
		int resInt = 0;
		resInt = adminRoleMapper.deleteRolePermission(roleId, name, primkey);
		return resInt;
	};

	public int addUsers(String roleId, List<String> userIds) {
		int ret = 0;
		AdminUserRole ur = new AdminUserRole(null, roleId);
		for (String userId : userIds) {
			ur.setUserId(userId);
			ret += adminUserRoleMapper.insert(ur);
		}
		return ret;
	}

	/**
	 * 删除角色-用户关系
	 * 
	 * @param roleId
	 * @return
	 */
	public int deleteUserRoleByRoleId(String roleId) {
		return adminUserRoleMapper.delete(new AdminUserRole(null, roleId));
	}

	/**
	 * 批量删除角色的用户
	 * 
	 * @param roleId
	 * @param userIds
	 * @return
	 */
	public int deleteUsers(String roleId, List<String> userIds) {
		int ret = 0;
		AdminUserRole ur = new AdminUserRole(null, roleId);
		for (String userId : userIds) {
			ur.setUserId(userId);
			ret = adminUserRoleMapper.delete(ur);
		}
		return ret;
	}

	/**
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public int deleteUserRole(String roleId, String userId) {
		return adminUserRoleMapper.delete(new AdminUserRole(userId, roleId));
	}

	public int addRolePermission(AdminRolePermission rolePermission) {
		int resInt = 0;
		resInt = adminRoleMapper.addRolePermission(rolePermission);
		return resInt;
	}

	public List<VRoleUser> searchUsers(String roleId, String keyword, String filter, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		if (StringUtils.isEmpty(keyword)) {
			keyword = StringUtils.trimToNull(keyword);
		} else {
			keyword = StringUtil.keywords(keyword);
		}
		return adminRoleMapper.searchUsersByRoleIdKeywordFilter(roleId, keyword, filter);
	}

	/**
	 * 获取用户的所有角色
	 * 
	 * @param userId
	 * @return
	 */
	public List<AdminRole> getRolesByUserId(String userId) {
		return adminRoleMapper.getRolesByUserId(userId);
	}

	public List<String> getRoleIdsByUserId(String userId) {
		List<String> ids = new ArrayList<String>();
		List<AdminUserRole> userRoles = adminUserRoleMapper.select(new AdminUserRole(userId, null));
		for (AdminUserRole ur : userRoles) {
			ids.add(ur.getRoleId());
		}
		return ids;
	}

	/**
	 * 通过roleName模糊查询Role
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<AdminRole> searchByRoleName(String keyword, int pageNum, int pageSize) {
		return selectByFields(AdminRole.class, new String[] { AdminRole.FIELD_ROLE_NAME }, keyword, pageNum, pageSize);
	}

	@Override
	public Class<?> getEntityClass() {
		return AdminRole.class;
	}
}
