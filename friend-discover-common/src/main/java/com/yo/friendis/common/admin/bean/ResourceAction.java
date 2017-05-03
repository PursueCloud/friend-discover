package com.yo.friendis.common.admin.bean;

import java.util.List;

public class ResourceAction {
	private String roleId;
	private String action;
	private List<String> adds;
	private List<String> deletes;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<String> getAdds() {
		return adds;
	}

	public void setAdds(List<String> adds) {
		this.adds = adds;
	}

	public List<String> getDeletes() {
		return deletes;
	}

	public void setDeletes(List<String> deletes) {
		this.deletes = deletes;
	}
}
