package com.yo.friendis.common.admin.controller;

import java.util.Date;

import com.yo.friendis.common.admin.service.AdminUserService;
import com.yo.friendis.common.common.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.yo.friendis.common.admin.model.AdminUser;
import com.yo.friendis.common.admin.util.UserUtil;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.controller.BaseController;

@Controller
@RequestMapping("admin/account")
public class AccountController extends BaseController {
	@Autowired
	AdminUserService adminUserService;

	@Override
	public ModelAndView index() {
		ModelAndView mav = super.index();
		return mav;
	}

	@RequestMapping("config")
	public ModelAndView config() {
		return new ModelAndView();
	}

	@RequestMapping("changePwd")
	@ResponseBody
	public Object changePwd(String oldPwd, String newPwd, String confirmPwd) {
		if (!StringUtils.equals(newPwd, confirmPwd)) {
			return new OperaterResult<Object>(false, "两次输入的新密码不一致");
		}
		AdminUser user = UserUtil.getCurrentUser();
		if (UserUtil.checkPassword(user.getAccount(), oldPwd)) {
//			newPwd = UserUtil.Sha256(newPwd);
			user.setPassword(newPwd);
			user.setModifyTime(DateUtils.getCurrentTimestamp());
			adminUserService.updateByPrimaryKeySelective(user);
			return new OperaterResult<Object>(true, "密码修改成功");
		}
		return new OperaterResult<Object>(false, "原密码错误");
	}
}
