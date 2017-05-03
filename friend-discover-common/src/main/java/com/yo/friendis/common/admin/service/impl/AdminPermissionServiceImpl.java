package com.yo.friendis.common.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yo.friendis.common.admin.mapper.AdminPermissionMapper;
import com.yo.friendis.common.admin.model.AdminPermission;
import com.yo.friendis.common.admin.service.AdminPermissionService;
import com.yo.friendis.common.common.service.AbstractService;

@Service("permissionService")
public class AdminPermissionServiceImpl extends AbstractService<AdminPermission> implements AdminPermissionService {
	@Autowired
	AdminPermissionMapper adminPermissionMapper;

	@Override
	public List<String> getPermissionByRole(String roleId) {
		return adminPermissionMapper.getPermissionByRole(roleId);
	}

	@Override
	public List<String> getPermissionByBtn(String roleId) {
		return adminPermissionMapper.getPermissionByBtn(roleId);
	}
}
