package com.yo.friendis.common.admin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yo.friendis.common.common.util.StringPool;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "admin_role")
public class AdminRole implements Serializable {
	private static final long serialVersionUID = -7456566921645491300L;
	//属性名常量
	public final static String FIELD_ROLE_ID = "roleId";
	public final static String FIELD_ROLE_NAME = "roleName";
	public final static String FIELD_CREATE_TIME= "createTime";
	public final static String FIELD_MODIFY_TIME = "modifyTime";
	public final static String FIELD_DESCRIPTION = "description";

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="Mysql")//不主动生成主键值,让mysql的auto_increment自动填充
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private String roleId;
	private String roleName;
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp createTime;
	@JsonFormat(pattern = StringPool.PATTERN_TIME, timezone = "GMT+8")
	private Timestamp modifyTime;
	private String description;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
