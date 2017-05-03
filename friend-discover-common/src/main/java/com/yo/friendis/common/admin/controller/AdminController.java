package com.yo.friendis.common.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.admin.service.AdminMenuService;
import com.yo.friendis.common.admin.util.EasyUIObjectConvertor;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.easyui.bean.MenuTreeNode;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {
	@Autowired
	AdminMenuService adminMenuService;

	@RequestMapping("login")
	public ModelAndView login() {
		return new ModelAndView();
	}

	/**
	 * @return
	 */
	@RequestMapping("getMenu")
	@ResponseBody
	public List<MenuTreeNode> getMenu() {
		List<AdminMenu> menus;
		menus = adminMenuService.selectAllRecursiveByParentId(AdminMenu.ROOT_ID);
		return EasyUIObjectConvertor.filterPermissionfromMenu(menus);
	}
	/**
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getMenuIndex")
	public List<Map<String,Object>> getMenuIndex(@RequestParam String pMenuId) {
		List<AdminMenu> menus = adminMenuService.getMenuIndex(pMenuId);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		Subject subject = SecurityUtils.getSubject();
		String permission = "Menu:view:";
		for (AdminMenu menu : menus) {
			String p = permission + menu.getMenuId();
			if (!subject.isPermitted(p)) {
				continue;
			}
			Map<String,Object> nodeMap = new HashMap<String,Object> ();
			nodeMap.put("id",menu.getMenuId());
			nodeMap.put("text",menu.getMenuName());
			nodeMap.put("url",menu.getUrl());
			nodeMap.put("type","index");
//			nodeMap.put("state","closed");
			listMap.add(nodeMap);
		}
		return listMap;
	}

}
