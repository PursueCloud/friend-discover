package com.yo.friendis.common.admin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.common.util.DateUtils;
import com.yo.friendis.common.common.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.yo.friendis.common.easyui.bean.MenuTreeNode;

public class EasyUIObjectConvertor {
	/**
	 * 转换树结构
	 * 
	 * @param menus
	 * @return
	 */
	public static List<MenuTreeNode> fromMenu(List<AdminMenu> menus) {
		List<MenuTreeNode> nodes = new ArrayList<MenuTreeNode>();
		for (AdminMenu menu : menus) {
			MenuTreeNode node = new MenuTreeNode();
			node.setId(menu.getMenuId() + "");
			node.setText(menu.getMenuName());
			node.setUrl(menu.getUrl());
			if (!menu.getSubMenu().isEmpty()) {
				node.setChildren(fromMenu(menu.getSubMenu()));
			}
			nodes.add(node);
		}
		return nodes;
	}

	/**
	 * 权限过滤，返回树结构菜单
	 * 
	 * @param menus
	 * @return
	 */
	public static List<MenuTreeNode> filterPermissionfromMenu(List<AdminMenu> menus) {
		List<MenuTreeNode> nodes = new ArrayList<MenuTreeNode>();
		Subject subject = SecurityUtils.getSubject();
		String permission = "Menu:view:";
		for (AdminMenu menu : menus) {
			String p = permission + menu.getMenuId();
			if (!subject.isPermitted(p)) {
				continue;
			}
			MenuTreeNode node = new MenuTreeNode();
			node.setId(menu.getMenuId() + "");
			node.setText(menu.getMenuName());
			node.setUrl(menu.getUrl());
			node.setIcon(menu.getIcon());
			if (!menu.getSubMenu().isEmpty()) {
				node.setChildren(filterPermissionfromMenu(menu.getSubMenu()));
			}
			nodes.add(node);
		}
		return nodes;
	}

	public static List<Map<String, Object>> covert2NodefromMenu(List<AdminMenu> menus) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (AdminMenu menu : menus) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("menuId", menu.getMenuId());
			map.put("menuName", menu.getMenuName());
			map.put("url", menu.getUrl());
			map.put("pMenuId", menu.getpMenuId());
			if ( AdminMenu.MenuType.PARENT.value == menu.getMenuType() ) {
				map.put("state", "closed");
			}
			map.put("description", menu.getDescription());
			map.put("createTime", DateUtils.formatTime(menu.getCreateTime()));
			map.put("modifyTime", DateUtils.formatTime(menu.getModifyTime()));
			list.add(map);
		}
		return list;
	}

}
