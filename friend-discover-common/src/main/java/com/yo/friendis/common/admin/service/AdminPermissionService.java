package com.yo.friendis.common.admin.service;

import java.util.List;

import com.yo.friendis.common.admin.model.AdminPermission;
import com.yo.friendis.common.common.service.BaseService;

public interface AdminPermissionService extends BaseService<AdminPermission> {
	List<String> getPermissionByRole(String roleId);
	
	List<String> getPermissionByBtn(String roleId);
}
