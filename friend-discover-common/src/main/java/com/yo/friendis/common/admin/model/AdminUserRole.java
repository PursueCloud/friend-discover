package com.yo.friendis.common.admin.model;

import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "admin_user_role")
public class AdminUserRole implements Serializable {
	private static final long serialVersionUID = 4537897687890150042L;
	@Id
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String userId;
	@Id
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String roleId;

	public AdminUserRole() {

	}

	public AdminUserRole(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
