package com.yo.friendis.common.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanUtil implements ApplicationContextAware {
	private static ApplicationContext ctx;

	public static Object getBean(String name) {
		return ctx.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return ctx.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return ctx.getBean(name, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType, Object... args) {
		return ctx.getBean(requiredType, args);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanUtil.ctx = applicationContext;

	}
}
