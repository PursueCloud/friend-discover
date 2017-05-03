package com.yo.friendis.common.admin.mapper;

import java.util.List;

import com.yo.friendis.common.admin.model.AdminPermission;

import tk.mybatis.mapper.common.Mapper;

public interface AdminPermissionMapper extends Mapper<AdminPermission> {
	/**
	 * 获取角色权限
	 * 
	 * @param roleId
	 * @return
	 */
	List<String> getPermissionByRole(String roleId);
	
	/**
	 * 获取根据角色按钮级别删除权限
	 * 
	 * @param roleId
	 * @return
	 */
	List<String> getPermissionByBtn(String roleId);
}
