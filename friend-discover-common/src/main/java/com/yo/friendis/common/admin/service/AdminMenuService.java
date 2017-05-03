package com.yo.friendis.common.admin.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.common.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.yo.friendis.common.admin.bean.PMenu;
import com.yo.friendis.common.admin.mapper.AdminMenuMapper;
import com.yo.friendis.common.admin.model.Resource;
import com.yo.friendis.common.common.service.AbstractService;
import com.yo.friendis.common.common.util.StringUtil;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service(AdminMenuService.BEAN_ID)
@CacheConfig(cacheNames = "com.yo.friendis.common.admin.Model.AdminMenu")
public class AdminMenuService extends AbstractService<AdminMenu> {
	public final static String BEAN_ID = "menuService";

	@Autowired
	AdminMenuMapper adminMenuMapper;
	@Autowired
	AdminRoleService adminRoleService;

	/**
	 * 递归获取所有菜单
	 * 
	 * @param pMenuId
	 * @return
	 */
	@Cacheable(key = "'recursive:menus:'+#pMenuId")
	public List<AdminMenu> selectAllRecursiveByParentId(String pMenuId) {
		return adminMenuMapper.selectAllRecursiveByParentId(pMenuId);
	}

	@Override
	@CacheEvict(allEntries = true)
	public int addSelective(AdminMenu entity) {
		entity.setMenuId("0");//设置主键为0，让auto_increment起效
		entity.setCreateTime(DateUtils.getCurrentTimestamp());
		return super.addSelective(entity);
	}

	@Override
	@CacheEvict(allEntries = true)
	public int updateByPrimaryKeySelective(AdminMenu entity) {
		entity.setModifyTime(DateUtils.getCurrentTimestamp());
		return super.updateByPrimaryKeySelective(entity);
	}
	
	@Override
	@CacheEvict(allEntries = true)
	public int deleteByPrimaryKeys(List<String> primaryKeys ) {
		return super.deleteByPrimaryKeys(primaryKeys);
	}

	@CacheEvict(allEntries = true)
	public void clearMenuCache() {
		logger.info("=============清空菜单缓存==========！");
	}
	/**
	 * 搜索菜单
	 * 
	 * @param keyword
	 * @param pMenuId
	 *            为null时，搜索所有
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<AdminMenu> selectByParentId(String keyword, String pMenuId, int pageNum, int pageSize) {
		Example example = new Example(AdminMenu.class);
		if (!StringUtils.isEmpty(keyword)) {
			keyword = StringUtil.keywords(keyword);
			example.createCriteria().andLike(AdminMenu.FIELD_MENU_NAME, keyword);
		}
		if ( StringUtils.isNotBlank(pMenuId)) {
			example.createCriteria().andEqualTo(AdminMenu.FIELD_P_MENU_ID, pMenuId);
		}
		PageHelper.orderBy("menu_order");
		PageHelper.startPage(pageNum, pageSize);
		return adminMenuMapper.selectByExample(example);
	}

	/**
	 * 根据id和菜单查询用户对于菜单管理的权限
	 * 
	 * @param roleId
	 *            角色id
	 * @param list
	 *            为菜单目录
	 */
	public void checkRoleMenuAuthType(String roleId, List<Map<String, Object>> list) {
		Map<String, Object> menuNodes = new HashMap<String, Object>();
		String authType = "Menu";
		if ( roleId != null ) {
			for (int i = 0; i < list.size(); i++) {
				menuNodes = list.get(i);
				String[] authPrimkey = { (String) menuNodes.get("menuId") };
				List<Resource> resources = adminRoleService.getResources(roleId, authType, authPrimkey);
				if (!resources.isEmpty()) {
					menuNodes.put("actions", resources.get(0).getActions());
				}
			}
		}
	}

	/**
	 * @param pMenuId
	 * @param roleId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<PMenu> selectMenusForPerms(String pMenuId, String roleId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return adminMenuMapper.selectMenusForPerms(pMenuId, roleId);
	}

	public List<AdminMenu> getMenuIndex(String pMenuId) {
		String[] fields = new String[]{"pMenuId","type"};
		String[] values = new String[]{pMenuId,"1"};
		Example example = new Example(AdminMenu.class);
		if (values != null && values.length > 0) {
			Criteria criteria = example.createCriteria();
			for (int i = 0; i < fields.length; i++) {
				if (i >= values.length) {
					break;
				}
				criteria.andEqualTo(fields[i], values[i]);
			}
		}
		return adminMenuMapper.selectByExample(example);
	}
	
	/**
	 * 
	 * 获取需要操作的所有菜单id[直到叶子结点]
	 * 
	 * 
	 * @param ids
	 * 				页面传过来的菜单id
	 * @return
	 * 
	 */
	public List<String> getFullMenusId(List<String> ids){
		
		//如果没有数据
		if(CollectionUtils.isEmpty(ids)){
			return Collections.emptyList();
		}
		
		List<String> childsMenuId;
		int parentCounter,childCounter;
		
		//遍历所有节点
		for(parentCounter = 0 ; parentCounter < ids.size() ; parentCounter++){
			
			//寻找当前结点的子节点
			childsMenuId = adminMenuMapper.selectChildsMenuIdByParentId(ids.get(parentCounter));
			//如果当前结点没有子节点则跳过
			if(CollectionUtils.isEmpty(childsMenuId)){
				continue;
			}
			
			//查找所有节点中是否包含当前结点的子节点
			childCounter = 0;
			for (;childCounter < childsMenuId.size();childCounter++) {
				if(ids.contains(childsMenuId.get(childCounter))){
					break;
				}
			}
			
			//如果不包含则把当前结点的子节点全部加上，如果包含则跳过继续
			if(childCounter == childsMenuId.size()){
				ids.addAll(childsMenuId);
			}else{
				continue;
			}
			
		}
		return ids;
	}

//	@Override
//	public Class<?> getEntityClass() {
//		return Menu.class;
//	}

}
