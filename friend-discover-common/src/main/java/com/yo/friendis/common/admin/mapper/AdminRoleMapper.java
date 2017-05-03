package com.yo.friendis.common.admin.mapper;

import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminRole;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import org.apache.ibatis.annotations.Param;

import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.admin.model.view.VRoleUser;

import tk.mybatis.mapper.common.Mapper;

public interface AdminRoleMapper extends Mapper<AdminRole> {
	/**
	 * @param con
	 * @return
	 */
	List<Map<String, Object>> getAdminRole(Map<String, Object> con);

	/**
	 * 获取角色的用户
	 * 
	 * @param roleId
	 * @param keyword
	 * @param filter
	 * @return
	 */
	List<VRoleUser> searchUsersByRoleIdKeywordFilter(@Param("roleId") String roleId, @Param("keyword") String keyword, @Param("filter") String filter);

	/**
	 * 获取用户所有角色
	 * 
	 * @param userId
	 * @return
	 */
	List<AdminRole> getRolesByUserId(String userId);

	List<String> getRoleIdsByUserId(@Param("userId") String userId);

	int updateAdminRole(Map<String, Object> role);

	void deleteAdminRole(List<String> ids);

	List<Resource> getResources(Map<String, Object> con);

	int updateRolePermission(AdminRolePermission rolePermission);

	int addRolePermission(AdminRolePermission rolePermission);

	int deleteRolePermission(@Param("roleId") String roleId, @Param("name") String name, @Param("primkey") String primkey);

	int delRoleAdmins(@Param("roleId") String roleId);

	int delUsersByRoleId(@Param("roleId") String roleId);

	int deleteUserRoleByRoleId(@Param("roleId") String roleId);
}
