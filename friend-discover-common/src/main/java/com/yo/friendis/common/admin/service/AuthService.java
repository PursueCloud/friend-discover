package com.yo.friendis.common.admin.service;

import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminRolePermission;
import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.service.BaseService;

public interface AuthService extends BaseService<AdminRolePermission> {

	/**
	 * 更新角色权限
	 * 
	 * @param roleId
	 * @param action
	 *            动作：view、update、delete、add等
	 * @param name
	 *            权限对象，如Menu、TagIndex,拓展可以使用类名
	 * @param primkey
	 *            具体对象的id
	 * @param checked
	 *            是否勾选
	 * @return
	 */
	public OperaterResult<String> updatePermission(String roleId, String action, String name, String primkey, String checked);

	/**
	 * 根据list获取权限对象数据
	 * 
	 * @param roleId
	 *            角色id
	 * @param name
	 *            权限对象
	 * @param primkeys
	 *            对象id集合
	 * @return
	 */
	public List<Resource> getResources(String roleId, String name, String[] primkeys);

	/**
	 * @return 所有菜单
	 */
	public List<AdminMenu> getMenus();

	/**
	 * 
	 * @param roleId
	 *            角色id
	 * @param resources
	 *            资源列表
	 * @param authType
	 *            权限类型，暂时为机构，标签目录，菜单目录，案例目录权限
	 */
	void checkRoleAuth(String roleId, List<Map<String, Object>> resources, String authType, String id);
}
