package com.yo.friendis.common.admin.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yo.friendis.common.admin.mapper.AdminUserRoleMapper;
import com.yo.friendis.common.admin.model.AdminUserRole;
import com.yo.friendis.common.common.service.AbstractService;

@Service
public class AdminUserRoleService extends AbstractService<AdminUserRole> {
	
	@Autowired
	private AdminUserRoleMapper adminUserRoleMapper;
	
	/**
	 * 根据 roleId 删除 AdminUserRole
	 * 
	 * @param roleId
	 * 
	 * @return
	 */
	public int deleteUserRoleByRoleId(@Param("roleId") String roleId){
		return	adminUserRoleMapper.deleteUserRoleByRoleId(roleId);
	}
	
	
}
