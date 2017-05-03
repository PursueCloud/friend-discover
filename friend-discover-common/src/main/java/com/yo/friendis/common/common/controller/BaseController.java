package com.yo.friendis.common.common.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yo.friendis.common.common.bean.OperaterException;
import com.yo.friendis.common.common.bean.OperaterResult;

public class BaseController {
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping
	public ModelAndView index() {
		String ctxPath = request.getContextPath();
		String uri = request.getRequestURI();
		uri = uri.substring(ctxPath.length());
		if (!uri.endsWith("/")) {
			uri += "/";
		}
		uri += "index";
		return new ModelAndView(uri);
	};

	@ExceptionHandler
	public @ResponseBody Object exception(Exception e) {
		e.printStackTrace();
		logger.error(e.getMessage(), e);
		OperaterResult<?> opt = new OperaterResult<Object>(false);
		if (e instanceof SQLException || e instanceof UncategorizedSQLException) {
			opt.getMeta().setMessage("请检查参数是否正确，执行数据库操作时出现错误。");
			opt.getMeta().setCode("521");
			return opt;
		}else if (e instanceof OperaterException) {
			return ((OperaterException)e).parseToOperaterResult();
		}
		opt.getMeta().setMessage("系统出错啦");
		opt.getMeta().setCode("520");
		return opt;
	}

}
