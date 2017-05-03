package com.yo.friendis.common.easyui.bean;

import java.util.List;

public interface TreeNode {
	/**
	 * id
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 实体id
	 * 
	 * @return
	 */
	String entryId();

	/**
	 * 节点显示标题
	 * 
	 * @return
	 */
	String getText();

	/**
	 * 状态
	 * 
	 * @return
	 */
	String getState();

	/**
	 * 图标
	 * 
	 * @return
	 */
	String getIcon();

	/**
	 * 节点类型
	 * 
	 * @return
	 */
	String getType();

	/**
	 * 子节点
	 * 
	 * @return
	 */
	List<?> getChildren();

}
