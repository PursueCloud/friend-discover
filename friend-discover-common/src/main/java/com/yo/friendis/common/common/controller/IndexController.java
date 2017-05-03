package com.yo.friendis.common.common.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Order(Ordered.LOWEST_PRECEDENCE)
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
	@RequestMapping("login")
	public ModelAndView login() {
		return new ModelAndView();
	}

	@RequestMapping("v/**")
	public ModelAndView path() {
		String ctxPath = request.getContextPath();
		String uri = request.getRequestURI();
		uri = uri.substring(ctxPath.length());
		if (uri != null) {
			if (!uri.contains("/v/")) {
				uri += "/";
			}
			uri = uri.replaceFirst("/v/", "/");
		}
		if (uri.endsWith("/")) {
			uri += "/index";
		}
		return new ModelAndView(uri);
	}

}
