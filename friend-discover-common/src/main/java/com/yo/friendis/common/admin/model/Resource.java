package com.yo.friendis.common.admin.model;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.List;

public class Resource {

	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String roleId;
	private String name, primkey;
	private List<String> actions;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrimkey() {
		return primkey;
	}

	public void setPrimkey(String primkey) {
		this.primkey = primkey;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}
}
