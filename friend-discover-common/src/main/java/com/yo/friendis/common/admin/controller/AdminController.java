package com.yo.friendis.common.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.yo.friendis.common.admin.util.ImageCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yo.friendis.common.admin.model.AdminMenu;
import com.yo.friendis.common.admin.service.AdminMenuService;
import com.yo.friendis.common.admin.util.EasyUIObjectConvertor;
import com.yo.friendis.common.common.controller.BaseController;
import com.yo.friendis.common.easyui.bean.MenuTreeNode;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {
	@Autowired
	AdminMenuService adminMenuService;
	@Autowired
	ImageCode imageCode;

	@RequestMapping("login")
	public ModelAndView login() {
		return new ModelAndView();
	}

	/**
	 * 获取验证码图片
	 */
	@RequestMapping("getCheckCodeImage")
	public void getCheckCodeImage() {
		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = null;
		try {
			sos = response.getOutputStream();
			ImageIO.write(imageCode.getImage(request), "jpeg", sos);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			"".split("");
			Pattern.compile("").split("");
			try {
				sos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				if (int.class == Integer.TYPE) {

				}
			}
		}
	}
	/**
	 * @return
	 */
	@RequestMapping("getMenu")
	@ResponseBody
	public List<MenuTreeNode> getMenu() {
		List<AdminMenu> menus;
		menus = adminMenuService.selectAllRecursiveByParentId(AdminMenu.ROOT_ID);
		return EasyUIObjectConvertor.filterPermissionfromMenu(menus);
	}
	/**
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getMenuIndex")
	public List<Map<String,Object>> getMenuIndex(@RequestParam String pMenuId) {
		List<AdminMenu> menus = adminMenuService.getMenuIndex(pMenuId);
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		Subject subject = SecurityUtils.getSubject();
		String permission = "Menu:view:";
		for (AdminMenu menu : menus) {
			String p = permission + menu.getMenuId();
			if (!subject.isPermitted(p)) {
				continue;
			}
			Map<String,Object> nodeMap = new HashMap<String,Object> ();
			nodeMap.put("id",menu.getMenuId());
			nodeMap.put("text",menu.getMenuName());
			nodeMap.put("url",menu.getUrl());
			nodeMap.put("type","index");
//			nodeMap.put("state","closed");
			listMap.add(nodeMap);
		}
		return listMap;
	}

}
