package com.yo.friendis.common.common.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class PageUtil {
	public static int getStart(int pageNum, int pageSize) {
		return pageSize * (pageNum - 1);
	}

	public static int getEnd(int pageNum, int pageSize) {
		return pageSize * pageNum;
	}

	/**
	 * 通过对比实体类返回排序语句
	 * 
	 * <pre>
	 * getOrderCause("p1,p2","asc,desc")   = "p1 asc,p2 desc"
	 * getOrderCause("p1,p2","asc")        = "p1 asc,p2"
	 * getOrderCause("","")      		   = ""
	 * getOrderCause(null,null)      		   = ""
	 * </pre>
	 * 
	 * 日后将修改为通过反射获取实体映射到数据库的字段名称，当前实现是直接把字段从驼峰转换为下划线形式
	 * 
	 * @param sort
	 * @param order
	 * @param entityClass
	 * @return
	 */
	public static String getOrderCause(String sort, String order, Class<?> entityClass) {
		String[] attrs = StringUtils.split(sort, ',');
		String[] orders = StringUtils.split(order, ',');
		StringBuilder sb = new StringBuilder();
		if (ArrayUtils.isNotEmpty(attrs)) {
			for (int i = 0; i < attrs.length; i++) {
				sb.append(StringUtil.camelToUnderline(attrs[i]));
				sb.append(StringPool.SPACE);
				if (orders != null && i < orders.length) {
					if (StringUtils.equalsIgnoreCase(orders[i], StringPool.ORDER_DESC)) {// 如果是逆序，则添加逆序
						sb.append(StringPool.ORDER_DESC);
					}
				}
				sb.append(StringPool.COMMA);
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}

}
