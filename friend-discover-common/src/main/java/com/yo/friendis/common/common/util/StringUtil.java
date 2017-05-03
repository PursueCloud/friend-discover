package com.yo.friendis.common.common.util;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import oracle.sql.TIMESTAMP;

@SuppressWarnings("rawtypes")
public class StringUtil {

	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String formatWithSecond(Object content) {
		if (content == null)
			return "";
		if (content instanceof Date) {
			return df2.format(content);
		}
		return content.toString();
	}

	/**
	 * <pre>
	 * format(String) = String
	 * format(int) = String
	 * format(BigDecimal) = String
	 * format(Time||Date||Timestamp||TIMESTAMP) = String
	 * </pre>
	 * 
	 * @param content
	 * @return
	 */
	public static String format(Object content) {
		if (content == null)
			return "";
		if (content instanceof Date || content instanceof Timestamp || content instanceof TIMESTAMP) {
			if (content instanceof TIMESTAMP) {
				try {
					return df.format(((TIMESTAMP) content).dateValue());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return df.format(content);
		}
		return content.toString();
	}

	public static String format(Object content, boolean isSecond) {
		if (content == null)
			return "";
		if (content instanceof Date) {
			if (isSecond)
				return df2.format(content);
			else
				return df.format(content);
		}
		return content.toString();
	}

	public static Date parse(String content) {
		if (StringUtils.isNotEmpty(content)) {
			try {
				return df2.parse(content);
			} catch (ParseException e) {
				try {
					return df.parse(content);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean isEmpty(Map map) {
		return map == null || map.size() < 1;
	}

	public static boolean isEmpty(List list) {
		return list == null || list.size() < 1;
	}

	static final String PATTERN_DIGIT = "\\d+";

	public static boolean isNumber(String value) {
		return (value != null && value.matches(PATTERN_DIGIT));
	}

	/**
	 * 格式化搜索字符串
	 * 
	 * <pre>
	 * StringUtil.keywords(null)          = null
	 * StringUtil.keywords("")            = null
	 * StringUtil.keywords("     ")       = null
	 * StringUtil.keywords("abc")         = "%abc%"
	 * StringUtil.keywords("    abc    ") = "%abc%"
	 * StringUtil.keywords("  a bc  ")    = "%a%bc%"
	 * </pre>
	 * 
	 * @param keyword
	 * @return
	 */
	public static String keywords(String keyword) {
		keyword = StringUtils.trimToNull(keyword);
		if (keyword == null || "".equals(keyword.trim())) {
			return null;
		}
		keyword = keyword.replaceAll("\\s+", "%");
		return "%" + keyword + "%";
	}

	/**
	 * 比较目标与一堆“字符串”
	 * 
	 * @param s1
	 * @param objs
	 * @return
	 */
	public static boolean equalAll(Object s1, Object... objs) {
		if (s1 == null || objs.length < 1) {
			return false;
		}
		for (Object o : objs) {
			if (!(String.valueOf(s1)).trim().toLowerCase().equals((String.valueOf(o)).trim().toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public static boolean contains(Object s1, Object s2) {
		if (s1 == null || s2 == null)
			return false;
		return (String.valueOf(s1)).trim().toLowerCase().contains((String.valueOf(s2)).trim().toLowerCase());
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

	public static final char UNDERLINE = '_';

	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String underlineToCamel2(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(param);
		Matcher mc = Pattern.compile("_").matcher(param);
		int i = 0;
		while (mc.find()) {
			int position = mc.end() - (i++);
			// String.valueOf(Character.toUpperCase(sb.charAt(position)));
			sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
		}
		return sb.toString();
	}
}
