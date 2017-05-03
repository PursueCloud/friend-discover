package com.yo.friendis.common.common.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("error")
public class ErrorController extends BaseController {

	@RequestMapping("{type}")
	public ModelAndView error(@PathVariable String type) {
		ModelAndView mav = new ModelAndView();
		if (StringUtils.equalsIgnoreCase(type, "null")) {

		}
		return mav;
	}
}
