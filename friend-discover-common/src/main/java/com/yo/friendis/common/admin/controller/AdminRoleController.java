package com.yo.friendis.common.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminUser;
import com.yo.friendis.common.admin.service.*;
import com.yo.friendis.common.common.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yo.friendis.common.admin.model.AdminRole;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import com.yo.friendis.common.admin.model.view.VRoleUser;
import com.yo.friendis.common.admin.service.AdminUserService;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.easyui.bean.DataGrid;
import com.yo.friendis.common.easyui.bean.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("admin/role")
public class AdminRoleController extends BaseController {

	@Autowired
	AdminRoleService adminRoleService;
	@Autowired
	AuthService authService;
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	AdminUserRoleService adminUserRoleService;
	@Autowired
	AdminRolePermissionService adminRolePermissionService;

	/**
	 * 获取角色列表
	 * 
	 * @param keyword
	 * @param page
	 * @return
	 */
	@RequestMapping("getRoles")
	@ResponseBody
	public Object getRoles(@RequestParam(required = false) String keyword, Page page) {
		List<AdminRole> list = null;
		list = adminRoleService.searchByRoleName(keyword, page.getPageNum(), page.getPageSize());
		PageInfo<AdminRole> pager = new PageInfo<AdminRole>(list);
		return new DataGrid(list, pager.getTotal());
	}

	/**
	 * 编辑页面
	 * 
	 * @param roleId
	 * @param name
	 * @return
	 */
	@RequestMapping("edit")
	public ModelAndView edit(@RequestParam(required = false) String roleId, @RequestParam(required = false) String name) {
		ModelAndView mav = new ModelAndView();
		if (!StringUtils.isEmpty(roleId)) {
			AdminRole role = adminRoleService.selectByPrimaryKey(roleId);
			mav.addObject("role", role);
			if (!StringUtils.isEmpty(name)) {
				if (name.equals("editAuth")) {
					mav.setViewName("admin/role/editAuth");
				} else if (name.equals("editUserRole")) {
					mav.setViewName("admin/role/editUserRole");
				}

			}
		}
		return mav;
	}

	/**
	 * 权限编辑页
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping("editPerms")
	public ModelAndView editPerms(String roleId) {
		ModelAndView mav = new ModelAndView("admin/role/editPerms");
		mav.addObject("roleId", roleId);
		return mav;
	}

	/**
	 * 保存
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Object save(AdminRole role) {
		int ret = 0;
		if (StringUtils.isEmpty(role.getRoleId())) {
			role.setRoleId("0");
			role.setCreateTime(DateUtils.getCurrentTimestamp());
			ret = adminRoleService.addSelective(role);
		} else {
			// 更新
			role.setModifyTime(DateUtils.getCurrentTimestamp());
			ret = adminRoleService.updateByPrimaryKeySelective(role);
		}
		return new OperaterResult<Object>(ret > 0 ? true : false, "");
	}

	@RequestMapping("del")
	@ResponseBody
	public Object del(@RequestParam("ids[]") List<String> ids) {
		int ret = 0;
		try {
			ret = adminRoleService.deleteByPrimaryKeys(ids);
			for (String roleId : ids) {
				adminRolePermissionService.deleteRolePermissionByRoleId(roleId);
				adminUserRoleService.deleteUserRoleByRoleId(roleId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new OperaterResult<Object>(ret > 0, "");
	}

	/**
	 * @param roleId
	 * @param name
	 * @param primkeys
	 * @return
	 */
	@RequestMapping("getResources")
	public Object getResources(String roleId, String name, String[] primkeys) {
		return new OperaterResult<List<?>>(true, "获取权限资源列表", adminRoleService.getResources(roleId, name, primkeys));
	}

	/**
	 * 更新权限
	 * 
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateRolePermission")
	public Object updateRolePermission(@RequestBody List<Object> data) {
		String primkey = null;
		// check仅仅作为按钮操作控制
		boolean check = true;
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> row = (Map<String, Object>) data.get(i);
			List<String> actions = (List<String>) row.get("actions");
			String name = (String) row.get("name");
			String roleId = (String) row.get("roleId");
			if (StringUtils.equalsIgnoreCase(name, "Menu")) {
				primkey = (String) row.get("menuId");
			} else if (StringUtils.equalsIgnoreCase(name, "Btn")) {
				name = (String) row.get("entity");
				primkey = (String) row.get("primkey");
				check = (Boolean) row.get("check");
			}
			adminRoleService.deleteRolePermission(roleId, name, primkey);
			if (!actions.isEmpty() && check) {
				for (int k = 0; k < actions.size(); k++) {
					adminRoleService.addRolePermission(new AdminRolePermission(roleId, actions.get(k), name, primkey));
				}
			}
		}
		return null;
	}

	/**
	 * @param roleId
	 * @param keyword
	 * @param filter
	 *            all、assigned,unassigned
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("searchUsers")
	public Object searchUsers(String roleId, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "all") String filter, Page page) {
		List<VRoleUser> list = adminRoleService.searchUsers(roleId, keyword, filter, page.getPageNum(), page.getPageSize());
		PageInfo<VRoleUser> pageInfo = new PageInfo<VRoleUser>(list);
		return new DataGrid(list, pageInfo.getTotal());
	}

	/**
	 * 更新角色-用户关系
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateUsers")
	public Object updateUsersRole(String roleId, @RequestParam(value = "addUserIds[]", defaultValue = "") List<String> addUserIds,
			@RequestParam(value = "delUserIds[]", defaultValue = "") List<String> delUserIds) {
		int ret = 0;
		if (delUserIds.isEmpty() && addUserIds.isEmpty()) {
			return new OperaterResult<String>(true, "没有任何更新");
		}
		//更新用户的修改时间
		List<String> updateUserIds = new ArrayList<String>();
		updateUserIds.addAll(addUserIds);
		updateUserIds.addAll(delUserIds);
		for(String userId : updateUserIds) {
			AdminUser updateUser = new AdminUser();
			updateUser.setUserId(userId);
			updateUser.setModifyTime(DateUtils.getCurrentTimestamp());
			adminUserService.updateByPrimaryKeySelective(updateUser);
		}
		//更新权限
		if (!delUserIds.isEmpty()) {
			ret = adminRoleService.deleteUsers(roleId, delUserIds);
		}
		if (!addUserIds.isEmpty()) {
			ret = adminRoleService.addUsers(roleId, addUserIds);
		}
		return new OperaterResult<Object>(ret > 0, "完成更新");
	}
}
