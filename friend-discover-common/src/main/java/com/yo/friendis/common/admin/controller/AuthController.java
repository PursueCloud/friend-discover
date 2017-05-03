package com.yo.friendis.common.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.admin.service.AdminMenuService;
import com.yo.friendis.common.admin.util.EasyUIObjectConvertor;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.easyui.bean.DataGrid;
import com.yo.friendis.common.easyui.bean.Page;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("admin/auth")
public class AuthController extends BaseController {

	@Autowired
	AdminUserService adminUserService;
	@Autowired
	AdminRoleService adminRoleService;
	@Autowired
	AuthService authService;
	@Autowired
	AdminMenuService adminMenuService;
	@Autowired
	AdminPermissionService adminPermissionService;
	@Autowired
	AdminRolePermissionService adminRolePermissionService;

	@RequestMapping("loadRoleAuth")

	public @ResponseBody Object loadRoleAuth(@RequestParam("authType") String authType, @RequestParam(value = "roleId", required = false) String roleId, @RequestParam(required = false) String keyword,
			@RequestParam(required = false) String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int rows) {
		List<AdminMenu> menus = null;
		if (!AdminMenu.ROOT_ID.equals(id)) {
			menus = adminMenuService.selectByParentId(keyword, id, 0, 0);
			List<Map<String, Object>> list = EasyUIObjectConvertor.covert2NodefromMenu(menus);
			authService.checkRoleAuth(roleId, list, authType, AdminMenu.FIELD_MENU_ID);
			return list;
		}
		menus = adminMenuService.selectByParentId(keyword, id, page, rows);
		List<Map<String, Object>> list = EasyUIObjectConvertor.covert2NodefromMenu(menus);
		authService.checkRoleAuth(roleId, list, authType, "menuId");
		PageInfo<AdminMenu> pager = new PageInfo<AdminMenu>(menus);
		return new DataGrid(list, pager.getTotal());
	}

	/**
	 * 获取控制按钮级别的权限
	 * 
	 * @param roleId
	 * 
	 * @return 源表集合
	 */
	@ResponseBody
	@RequestMapping("loadBtnAuth")
	public Object loadBtnAuth(@RequestParam(value = "roleId", required = false) String roleId) {
		List<Map<Object, Object>> btnMapList = new ArrayList<Map<Object, Object>>();
		List<String> permissionList = adminPermissionService.getPermissionByBtn(roleId);
		for (String s : permissionList) {
			Map<Object, Object> btnMap = new HashMap<Object, Object>();
			String[] per = s.split(":");
			btnMap.put(per[1], per[0]);
			btnMapList.add(btnMap);
		}
		return btnMapList;
	}

	/**
	 * 获取带权限属性的资源
	 * 
	 * @param roleId
	 * @param keyword
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping("getResources")
	public @ResponseBody Object getResources(String roleId, String keyword, String name, Page page) {
		// TODO 过滤敏感权限输入,待完成
		List<Resource> list = adminRolePermissionService.search(roleId, keyword, name, page.getPageNum(), page.getPageSize());
		PageInfo<Resource> pinf = new PageInfo<Resource>(list);
		return new DataGrid(list, pinf.getTotal());
	}

}
