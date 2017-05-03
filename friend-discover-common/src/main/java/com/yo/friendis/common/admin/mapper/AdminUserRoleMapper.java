package com.yo.friendis.common.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.yo.friendis.common.admin.model.AdminUserRole;

import tk.mybatis.mapper.common.Mapper;

public interface AdminUserRoleMapper extends Mapper<AdminUserRole> {
	/**
	 * 根据 roleId 删除 AdminUserRole
	 * 
	 * @param roleId
	 * 
	 * @return
	 */
	int deleteUserRoleByRoleId(@Param("roleId") String roleId);
}
