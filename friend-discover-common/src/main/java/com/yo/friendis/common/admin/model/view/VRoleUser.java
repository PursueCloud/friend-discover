package com.yo.friendis.common.admin.model.view;

import com.yo.friendis.common.admin.model.AdminUser;

/**
 * 角色中，分配用户的用户列表视图
 * 
 * @author yhl
 *
 */
public class VRoleUser extends AdminUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4776083774540658078L;

	private boolean assigned;

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
}
