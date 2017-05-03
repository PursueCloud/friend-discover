package com.yo.friendis.common.admin.permissionhelper;

/**
 * 权限过滤sql拦截参数
 * 
 * @author yhl
 *
 */
public class Permission {
	private String name;
	private String action;
	private String keyColumn;
	private String userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
