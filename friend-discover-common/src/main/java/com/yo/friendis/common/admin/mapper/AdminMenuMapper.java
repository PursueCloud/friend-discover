package com.yo.friendis.common.admin.mapper;

import java.util.List;

import com.yo.friendis.common.admin.model.AdminMenu;
import org.apache.ibatis.annotations.Param;
import com.yo.friendis.common.admin.bean.PMenu;

import tk.mybatis.mapper.common.Mapper;

public interface AdminMenuMapper extends Mapper<AdminMenu> {
	List<AdminMenu> selectAllRecursiveByParentId(@Param("pMenuId") String pMenuId);

	List<PMenu> selectMenusForPerms(@Param("pMenuId") String pMenuId, @Param("roleId") String roleId);

	List<String> selectChildsMenuIdByParentId(@Param("pMenuId") String pMenuId);
}
