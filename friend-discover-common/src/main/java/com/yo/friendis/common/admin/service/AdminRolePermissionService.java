package com.yo.friendis.common.admin.service;

import java.util.Collection;
import java.util.List;

import com.yo.friendis.common.admin.model.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import com.yo.friendis.common.admin.mapper.AdminRolePermissionMapper;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.common.service.AbstractService;
import com.yo.friendis.common.common.util.StringUtil;
import com.yo.friendis.common.shiro.realms.Realm;
import com.github.pagehelper.PageHelper;

@Service(AdminRolePermissionService.BEAN_ID)
public class AdminRolePermissionService extends AbstractService<AdminRolePermission> {
	public final static String BEAN_ID = "rolePermissionService";
	@Autowired
	AdminRolePermissionMapper adminRolePermissionMapper;

	/**
	 * 添加授权
	 * 
	 * @param roleId
	 * @param action
	 * @param permission
	 * @param ids
	 * @return
	 */
	public int addPermission(String roleId, String action, String permission, List<String> ids) {
		int ret = 0;
		for (String id : ids) {
			AdminRolePermission rp = new AdminRolePermission(roleId, action, permission, id);
			ret += addSelective(rp);
		}
		return ret;
	}
	
	/**
	 * 添加授权
	 * 
	 * @param rps
	 * @return
	 */
	public int addPermission(Collection<AdminRolePermission> rps) {
		int ret = 0;
		for (AdminRolePermission rp : rps) {
			ret += addSelective(rp);
		}
		return ret;
	}
	

	/**
	 * 复制授权信息
	 * 
	 * @param name
	 * @param source
	 * @param dest
	 * @return
	 */

	@Caching(evict = { @CacheEvict(value = Realm.CACHE_NAME_1, allEntries = true),
			@CacheEvict(value = Realm.CACHE_NAME_2, allEntries = true) })
	public int clonePermission(String name, String source, String dest) {
		return adminRolePermissionMapper.cloneRolePermission(name, source, dest);
	}

	/**
	 * 移除授权
	 * 
	 * @param roleId
	 * @param action
	 * @param permission
	 * @param deletes
	 * @return
	 */
	public int removePermission(String roleId, String action, String permission, List<String> deletes) {
		int ret = 0;
		for (String id : deletes) {
			ret += delete(new AdminRolePermission(roleId, action, permission, id));
		}
		return ret;
	}
	
	/**
	 * 移除授权
	 * @return
	 */
	public int removePermission(Collection<AdminRolePermission> rps) {
		int ret = 0;
		for (AdminRolePermission rp : rps) {
			ret += delete(rp);
		}
		return ret;
	}
	

	public List<Resource> search(String roleId, String keyword, String name, int pageNum, int pageSize) {
		keyword = StringUtil.keywords(keyword);
		PageHelper.startPage(pageNum, pageSize);
		return adminRolePermissionMapper.searchResources(roleId, keyword, name);
	}

	/**
	 * 获取所有的RolePermission
	 * 
	 * @param action
	 * @param name
	 * @param primkey
	 * @return
	 */
	public List<AdminRolePermission> getRolePermissions(String action, String name, String primkey) {
		AdminRolePermission rp = new AdminRolePermission();
		rp.setAction(action);
		rp.setName(name);
		rp.setPrimkey(primkey);
		return adminRolePermissionMapper.select(rp);
	}

	/**
	 * 根据 roleId 删除 RolePermission
	 * 
	 * @param roleId
	 * 
	 * @return
	 */
	public int deleteRolePermissionByRoleId(String roleId) {
		return adminRolePermissionMapper.deleteRolePermissionByRoleId(roleId);
	}

}
