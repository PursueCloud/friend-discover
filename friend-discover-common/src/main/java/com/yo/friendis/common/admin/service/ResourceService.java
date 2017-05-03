package com.yo.friendis.common.admin.service;

import java.util.List;

import com.yo.friendis.common.admin.mapper.AdminRolePermissionMapper;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.common.service.AbstractService;

public class ResourceService extends AbstractService<AdminRolePermission> {
	private AdminRolePermissionMapper adminRolePermissionMapper;

	/**
	 * 
	 * @param roleId
	 * @return 角色对资源的列表
	 */
	@Deprecated
	public List<Resource> selectByRoleId(String roleId) {
		return adminRolePermissionMapper.selectByRoleId(roleId);
	}
}
