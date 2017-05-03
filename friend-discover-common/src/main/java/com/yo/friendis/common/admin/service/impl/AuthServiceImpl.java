package com.yo.friendis.common.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yo.friendis.common.admin.mapper.AdminRolePermissionMapper;
import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import com.yo.friendis.common.admin.service.AuthService;
import com.yo.friendis.common.admin.service.AdminRoleService;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.service.AbstractService;

/**
 * 
 * @author yhl
 *
 */
@Service("authService")
public class AuthServiceImpl  extends AbstractService<AdminRolePermission> implements AuthService{
	@Autowired
	AdminRolePermissionMapper authMapper;
	@Autowired
	AdminRoleService adminRoleService;
	Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 更新角色权限
	 * 
	 * @param roleId 角色id
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
	public OperaterResult<String> updatePermission(String roleId, String action, String name, String primkey, String checked) {
		try {
			if (checked != null && ("on".equals(checked) || "true".equals(checked))) {
				authMapper.addRolePermission(new AdminRolePermission(roleId, action, name, primkey));
			} else {
				authMapper.deleteRolePermission(roleId, action, name, primkey);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return new OperaterResult<String>(true, "操作权限失败");
		}
		return  new OperaterResult<String>(true, "操作成功");
	}

	/**
	 * 获取权限对象所有数据
	 * 
	 * @param roleId
	 *            角色id
	 * @param name
	 *            权限对象
	 * @return
	 */
	public List<Resource> getResources(String roleId, String name) {
		return getResources(roleId, name, null);
	}

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
	public List<Resource> getResources(String roleId, String name, String[] primkeys) {
		Map<String, Object> con = new HashMap<String, Object>();
		con.put("roleId", roleId);
		con.put("name", name);
		con.put("primkeys", primkeys);
		return authMapper.getResources(con);
	}
	
	/**
	 * @param roleId
	 * @return 根据用户的权限显示具体的菜单
	 */
	public List<AdminMenu> getMenusPerms(String roleId) {
		return authMapper.getMenusPerms(roleId);
	}

	public List<AdminMenu> getMenus() {
		return authMapper.getMenus();
	}

	/**
	 * @param roleId
	 * 			 角色id
	 * @param userId
	 * 			用户id
	 * @param op
	 * 		 操作，add增加，delete删除
	 * @return  
	 */
	public Object updateRoleUser(String roleId, String userId, String op) {
		String msg = null;
		if ("add".equalsIgnoreCase(op)) {
			authMapper.addUserRole(userId, roleId);
			msg = "角色添加用户";
		} else {
			authMapper.deleteUserRole(userId, roleId);
			msg = "角色移除用户";
		}
		return new OperaterResult<Object>(true, msg);
	}

	@Override
	public void checkRoleAuth(String roleId, List<Map<String, Object>> primkeyList,String authType,String id) {
		Map<String,Object> node =  new HashMap<String,Object>();
		if( roleId != null ){
			for(int i = 0; i < primkeyList.size(); i++){  
				node = primkeyList.get(i);
				String[] authPrimkey={(String) node.get(id)};
				List<Resource> resources = adminRoleService.getResources(roleId, authType,authPrimkey);
				if(!resources.isEmpty()){
					List<String> actions = new ArrayList<String>();
					for(Resource res : resources) {
						actions.addAll(res.getActions());
					}
					node.put("actions", actions);
				}
		    }  
		}		
	}

}
