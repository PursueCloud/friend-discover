package com.yo.friendis.common.admin.bean;

import java.util.List;

import com.yo.friendis.common.admin.model.AdminMenu;
import org.apache.commons.lang.StringUtils;
import com.yo.friendis.common.easyui.bean.TreeNode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PMenu implements TreeNode {
	private String menuId, menuName, icon, type;

	private List<String> actions;
	private List<PMenu> children;

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	@Override
	public String getId() {
		return this.menuId;
	}

	@Override
	public String entryId() {
		return this.menuId;
	}

	@Override
	public String getText() {
		return this.menuName;
	}

	@Override
	public String getState() {
		if (StringUtils.equalsIgnoreCase(this.type, AdminMenu.MenuType.LEAF.valStr)) {
			return "open";
		}
		return "closed";

	}

	@Override
	public String getIcon() {
		return this.icon;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	@JsonInclude(value = Include.NON_NULL)
	public List<?> getChildren() {
		return this.children;
	}

	public void setChildren(List<PMenu> children) {
		this.children = children;
	}

}
