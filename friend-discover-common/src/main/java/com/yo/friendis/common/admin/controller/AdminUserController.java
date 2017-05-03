package com.yo.friendis.common.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yo.friendis.common.admin.service.AdminUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yo.friendis.common.admin.model.AdminRole;
import com.yo.friendis.common.admin.model.AdminUser;
import com.yo.friendis.common.admin.service.AdminRoleService;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.easyui.bean.DataGrid;
import com.yo.friendis.common.easyui.bean.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("admin/user")
public class AdminUserController extends BaseController {
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	AdminRoleService adminRoleService;

	@RequestMapping("ctlName")
	public @ResponseBody Object getCtlName() {
		return new OperaterResult<Object>(true, getClass().getName());
	}

	/**
	 * @param page
	 * @param keyword
	 * @return 所有用户或特定用户
	 */
	@ResponseBody
	@RequestMapping("/getUsers")
	public Object getUser(@RequestParam(required = false) String keyword, Page page) {
		List<AdminUser> list = adminUserService.selectByFields(new String[] { AdminUser.FIELD_ACCOUNT, AdminUser.FIELD_USER_NAME }, keyword, page.getPageNum(), page.getPageSize());
		PageInfo<AdminUser> pageInfo = new PageInfo<AdminUser>(list);
		return new DataGrid(list, pageInfo.getTotal());
	}

	@ResponseBody
	@RequestMapping("/getUserRole")
	public Object getUserRole(@RequestParam(required = true) String userId, Page page) {
		Map<String, Object> resp = new HashMap<String, Object>();
		List<AdminRole> list = adminRoleService.selectPage(page.getPageNum(), page.getPageSize());
		List<String> roldIdList = adminRoleService.getRoleIdsByUserId(userId);
		PageInfo<AdminRole> pages = new PageInfo<AdminRole>(list);
		resp.put("rows", list);
		resp.put("total", pages.getTotal());
		resp.put("userRoleIds", roldIdList);
		return resp;
	}

	/**
	 * 判断新增抑或修改
	 * 
	 * @param userId
	 * @param editPasFlag
	 * @return 相应页面
	 */
	@RequestMapping("edit")
	public ModelAndView edit(@RequestParam(required = false) String userId, @RequestParam(required = false) String editPasFlag) {
		ModelAndView mav = new ModelAndView();
		if (!StringUtils.isEmpty(userId)) {
			AdminUser user = adminUserService.selectByPrimaryKey(userId);
			mav.addObject("user", user);
			if (!StringUtils.isEmpty(editPasFlag)) {
				mav.setViewName("admin/user/editPas");
			}
		}
		return mav;
	}

	@ResponseBody
	@RequestMapping("editPas")
	public Object editPas(@RequestParam("id") String userId, @RequestParam("newPas") String newPas) {
//		newPas = UserUtil.Sha256(newPas);
		int ret = adminUserService.editRolePas(userId, newPas);
		return new OperaterResult<String>(ret > 0);
	}

	/**
	 * 新增或修改用户
	 */
	@ResponseBody
	@RequestMapping("save")
	public Object save(AdminUser user, @RequestParam(name = "roleIds", required = false) List<String> roleIds) {
		// 在新增或保存前检查用户是否已经存在
		AdminUser userCheck = adminUserService.getUserByAccount(user.getAccount());
		if (userCheck != null) {
			return new OperaterResult<String>(false, "账户已存在");
		}
		user = adminUserService.save(user, roleIds);
		return new OperaterResult<String>(user != null);
	}

	@ResponseBody
	@RequestMapping("del")
	public Object del(@RequestParam("ids[]") List<String> ids) {
		int ret = 0;
		ret = adminUserService.deleteByPrimaryKeys(ids);
		for (int i = 0; i < ids.size(); i++) {
			adminUserService.deleteUserRoleByUserId(ids.get(i));
		}
		return new OperaterResult<String>(ret > 0);
	}

}
