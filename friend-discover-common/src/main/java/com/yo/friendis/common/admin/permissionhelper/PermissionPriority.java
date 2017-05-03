package com.yo.friendis.common.admin.permissionhelper;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.util.StringUtils;

/**
 * 权限优先级
 * 
 * @author walidake
 *
 */
public enum PermissionPriority {

	//TODO 后面可通过构造shiro AdminPermission parts部分数据来实现
	VIEW("view", 0x00),
	EDIT("edit", 0x01);
	//ANY("*", 0xff)

	/**
	 * 获取最大权限action
	 * 获取失败情况下返回原有action
	 * 
	 * 
	 * @param action
	 * @return
	 */
	public static List<String> getMaxPermissionActions(String action) {
		
		//获取当前优先级
		int currentPriority = getActionPriority(action);
		
		List<String> actions = new ArrayList<String>();
		if(currentPriority != -1){
			for(PermissionPriority pp : PermissionPriority.values()){
				if(currentPriority >= pp.getPriority()){
					actions.add(pp.action);
				}
			}
		}else{
			actions.add(action);
		}
		return actions;
	}
	
	
	/**
	 * 获取最小权限action
	 * 获取失败情况下返回原有action
	 * 
	 * 
	 * @param action
	 * @return
	 */
	public static List<String> getMinPermissionActions(String action) {
		
		//获取当前优先级
		int currentPriority = getActionPriority(action);
		List<String> actions = new ArrayList<String>();
		if(currentPriority != -1){
			for(PermissionPriority pp : PermissionPriority.values()){
				if(currentPriority <= pp.getPriority()){
					actions.add(pp.action);
				}
			}
		}else{
			actions.add(action);
		}
		return actions;
	}
	
	/**
	 * 获取action优先级
	 * 
	 * 无法获取优先级返回-1
	 * 
	 * @param action
	 * @return
	 */
	public static int getActionPriority(String action){
		if(StringUtils.isEmpty(action)){
			return -1;
		}
		
		for(PermissionPriority pp : PermissionPriority.values()){
			if(action.equalsIgnoreCase(pp.getAction())){
				return pp.getPriority();
			}
		}
		
		return -1;
	}
	
	
	private String action;
	private int priority;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	private PermissionPriority(String action, int priority) {
		this.action = action;
		this.priority = priority;
	}
}
