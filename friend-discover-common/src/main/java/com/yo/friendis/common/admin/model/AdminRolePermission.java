package com.yo.friendis.common.admin.model;

import java.io.Serializable;

import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

@Table(name = "admin_role_permission")
public class AdminRolePermission implements Serializable {
	private static final long serialVersionUID = 7809729582719423513L;
	public static final String FIELD_ROLE_ID = "roleId";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_ACTION = "action";
	public static final String FIELD_PRIMKEY = "primkey";
	public static final String FIELD_PERMISSION = "permission";
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String roleId;
	private String name, action, primkey;
	/**
	 * eg. Menu:view:10001 =&gt表示对菜单id为10001具有可见权限
	 */
	private String permission;

	public AdminRolePermission() {
	};

	public AdminRolePermission(String roleId) {
		this(roleId, null, null, null);
	};

	public AdminRolePermission(String roleId, String action, String name, String primkey) {
		this.roleId = roleId;
		this.action = action;
		this.name = name;
		this.primkey = primkey;
		if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(action) && StringUtils.isNotEmpty(primkey)) {
			this.permission = String.format("%s:%s:%s", name, action, primkey);
		}
	};

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
	
	@Override
	public boolean equals(Object obj) {
		//同一个对象||permission相同
		return (this == obj) ||
				((obj instanceof AdminRolePermission) && !StringUtils.isEmpty(permission)
						&& permission.equals(((AdminRolePermission)obj).getPermission()));
	}
	
	@Override
	public int hashCode() {
		return permission.hashCode() * 31 + 17;
	}
	
}
