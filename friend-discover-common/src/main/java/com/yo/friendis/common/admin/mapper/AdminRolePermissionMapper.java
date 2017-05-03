package com.yo.friendis.common.admin.mapper;

import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import org.apache.ibatis.annotations.Param;
import com.yo.friendis.common.admin.model.Resource;

import tk.mybatis.mapper.common.Mapper;

public interface AdminRolePermissionMapper extends Mapper<AdminRolePermission> {
	int addRolePermission(AdminRolePermission rolePermission);

	int deleteRolePermission(@Param("roleId") String roleId, @Param("action") String action, @Param("name") String name, @Param("primkey") String primkey);

	/**
	 * 复制授权记录
	 * 
	 * @param name
	 *            类型
	 * @param source
	 *            源主键
	 * @param dest
	 *            目标主键
	 * @return
	 */
	int cloneRolePermission(@Param("name") String name, @Param("source") String source, @Param("dest") String dest);

	/**
	 * @param con
	 *            <ul>
	 *            <li>roleId</li>
	 *            <li>name</li>
	 *            <li>(optional)List(String)</li>
	 *            </ul>
	 * @return
	 */

	/**
	 * 更新角色权限<br>
	 * Map{<br>
	 * roleId:<br>
	 * action: 动作：view、update、delete、add等<br>
	 * name： 权限对象，如Menu、TagIndex,拓展可以使用类名<br>
	 * primkey： 具体对象的id<br>
	 * checked： 是否勾选<br>
	 * }
	 */
	List<Resource> getResources(Map<String, Object> con);

	/**
	 * @param roleId
	 * @return 根据用户的权限显示具体的菜单
	 */
	List<AdminMenu> getMenusPerms(String roleId);

	List<AdminMenu> getMenus();

	int addUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

	int deleteUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

	/**
	 * 根据角色获取资源
	 * 
	 * @param roleId
	 * @return
	 */
	List<Resource> selectByRoleId(String roleId);

	List<Resource> searchResources(@Param("roleId") String roleId, @Param("keyword") String keyword, @Param("name") String name);

	/**
	 * 根据 roleId 删除 RolePermission
	 * 
	 * @param roleId
	 * 
	 * @return
	 */
	int deleteRolePermissionByRoleId(@Param("roleId") String roleId);
}
