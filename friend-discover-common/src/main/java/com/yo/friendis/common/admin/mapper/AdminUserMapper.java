package com.yo.friendis.common.admin.mapper;

import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminUser;
import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface AdminUserMapper extends Mapper<AdminUser> {
	List<AdminUser> getUsers();

	AdminUser getUserByUserId(String userId);

	List<AdminUser> getUsersByRole(@Param("roleId") String roleId);

	String getUserIdByName(String userName);

	AdminUser getUserByUserName(String userName);

	int countAdminUser();

	List<AdminUser> getAdminUser(Map<String, Object> nameParam);

	int addUserRole(@Param("roleId") String roleId, @Param("userId") String userId);

	int deleteUserRoleByUserId(@Param("userId") String userId);

	int editRolePas(@Param("userId") String userId, @Param("newPas") String newPas);

	List<AdminUser> getUnSelUsersByRoleId(@Param("roleId") String roleId);

	List<AdminUser> getUsersByRoleId(@Param("roleId") String roleId);

	List<AdminUser> getUsersByRoleIdAndLike(@Param("keyword") String keyword, @Param("roleId") String roleId);

	List<AdminUser> getUnSelUsersByRoleIdAndLike(@Param("keyword") String keyword, @Param("roleId") String roleId);
}

