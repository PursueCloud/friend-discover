package com.yo.friendis.common.admin.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.admin.model.AdminRolePermission;
import com.yo.friendis.common.admin.service.AdminMenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yo.friendis.common.admin.bean.PMenu;
import com.yo.friendis.common.admin.bean.ResourceAction;
import com.yo.friendis.common.admin.permissionhelper.PermissionPriority;
import com.yo.friendis.common.admin.service.AdminRolePermissionService;
import com.yo.friendis.common.admin.util.EasyUIObjectConvertor;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.easyui.bean.DataGrid;
import com.yo.friendis.common.easyui.bean.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("admin/menu")
public class AdminMenuController extends BaseController {
	@Autowired
	AdminMenuService adminMenuService;
	@Autowired
	AdminRolePermissionService adminRolePermissionService;
	private static final String PERMISSION_NAME = "Menu";

	/**
	 * 异步树表格
	 * 
	 * @param keyword
	 * @param id
	 * @param isRoot
	 * @param page
	 * @return
	 */
	@RequestMapping("searchMenus")
	public @ResponseBody Object searchMenus(@RequestParam(required = false) String keyword, @RequestParam(required = false) String id, @RequestParam(defaultValue = "false") boolean isRoot,
			Page page) {
		List<AdminMenu> menus = null;
		if (!isRoot) {
			menus = adminMenuService.selectByParentId(keyword, id, 0, 0);
			List<Map<String, Object>> list = EasyUIObjectConvertor.covert2NodefromMenu(menus);
			return list;
		}
		menus = adminMenuService.selectByParentId(keyword, id, page.getPageNum(), page.getPageSize());
		List<Map<String, Object>> list = EasyUIObjectConvertor.covert2NodefromMenu(menus);
		PageInfo<AdminMenu> pager = new PageInfo<AdminMenu>(menus);

		return new DataGrid(list, pager.getTotal());
	}

	@RequestMapping("edit")
	public ModelAndView edit(@RequestParam(required = false) String menuId, @RequestParam(defaultValue = "0") String pMenuId) {
		ModelAndView mav = new ModelAndView();
		if (!StringUtils.isEmpty(menuId)) {
			AdminMenu menu = adminMenuService.selectByPrimaryKey(menuId);
			pMenuId = menu.getpMenuId();
			mav.addObject("menu", menu);
		}
		mav.addObject("pMenuId", pMenuId);
		return mav;
	}

	@RequestMapping("save")
	public @ResponseBody Object save(AdminMenu menu) {
		int ret = 0;
		try {
			if (StringUtils.isEmpty(menu.getpMenuId())) {
				menu.setpMenuId(AdminMenu.ROOT_ID);
			}
			if (StringUtils.isEmpty(menu.getMenuId())) {
				ret = adminMenuService.addSelective(menu);
			} else {
				// 如果需要定制只更新某些属性或者在某些条件情况下才更新，则自行编写逻辑
				// 类似代码：
				/*
				 * Menu entity = adminMenuService.selectByPrimaryKey(menu.getMenuId()); entity.setMenuName(menu.getMenuName()); if(menu.getMenuUrl() 满足某些条件){ entity.setMenuUrl(menu.getMenuUrl()); }
				 * adminMenuService.update(entity);
				 */
				ret = adminMenuService.updateByPrimaryKeySelective(menu);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Integer.toBinaryString(1);
		}

		return new OperaterResult<Object>(ret > 0);
	}

	@RequestMapping("del")
	@CacheEvict(allEntries = true)
	public @ResponseBody Object del(@RequestParam("ids[]") List<String> ids) {
		adminMenuService.deleteByPrimaryKeys(ids);
		return new OperaterResult<Object>(true, "删除成功");
	}

	@RequestMapping("config")
	public ModelAndView config(String roleId) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("roleId", roleId);
		return mav;
	}

	@RequestMapping("clearMenuCache")
	public @ResponseBody Object clearMenuCache() {
		adminMenuService.clearMenuCache();
		return new OperaterResult(true);
	}
	/**
	 * 获取带权限属性的标签目录树
	 * 
	 * @param id
	 * @param roleId
	 * @param page
	 * @return
	 */
	@RequestMapping("loadPerms")
	public @ResponseBody Object loadPerms(@RequestParam(defaultValue = "0") String id, @RequestParam(value = "roleId", required = false) String roleId, Page page) {
		List<PMenu> list = null;
		if (!StringUtils.equalsIgnoreCase(id, "0")) {
			list = adminMenuService.selectMenusForPerms(id, roleId, 0, 0);
			return list;
		}
		list = adminMenuService.selectMenusForPerms(id, roleId, page.getPageNum(), page.getPageSize());
		PageInfo<PMenu> pageInfo = new PageInfo<PMenu>(list);
		return new DataGrid(list, pageInfo.getTotal());
	}

	@RequestMapping("savePerms")
	public @ResponseBody Object savePerms(@RequestBody List<ResourceAction> list) {
		int ret = 0;
		
		List<String> adds,deletes;
		List<String> actions;
		Set<AdminRolePermission> addRps = new HashSet<AdminRolePermission>();
		Set<AdminRolePermission> deleteRps = new HashSet<AdminRolePermission>();
		
		for (ResourceAction rc : list) {
			// TODO 需要过滤敏感权限,待完成

			//查找到所有需要添加的菜单节点id
			adds = adminMenuService.getFullMenusId(rc.getAdds());
			for (String id : adds) {
				//增加取得最大权限
				//例如：编辑必定可见
				actions = PermissionPriority.getMaxPermissionActions(rc.getAction());
				for (String action : actions) {
					addRps.add(new AdminRolePermission(rc.getRoleId(), action, PERMISSION_NAME, id));
				}
			}
			
			//查找到所有需要删除的菜单节点id
			deletes = adminMenuService.getFullMenusId(rc.getDeletes());
			for (String id : deletes) {
				//删除取得最小权限
				//例如：不可见必定不可编辑
				actions = PermissionPriority.getMinPermissionActions(rc.getAction());
				for (String action : actions) {
					deleteRps.add(new AdminRolePermission(rc.getRoleId(), action, PERMISSION_NAME, id));
				}
			}
		}
		
		ret += adminRolePermissionService.addPermission(addRps);
		ret += adminRolePermissionService.removePermission(deleteRps);
		
		return new OperaterResult<Object>(ret > 0, ret > 0 ? "" : "没有添加或者删除任何权限");
	}
}
