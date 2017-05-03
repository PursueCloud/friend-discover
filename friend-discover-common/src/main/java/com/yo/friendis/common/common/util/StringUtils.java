package com.yo.friendis.common.common.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * @author jay.huang
 *
 */
public class StringUtils {
	/**
	 * 第一个字母转化为大写
	 * @param str
	 * @return
	 */
	public static String firstCharToUpperCase(String str) {
		String result = str.substring(0,1).toUpperCase() + str.substring(1);
		return result;
	}

	/**
	 * 处理目标字符串中不符合json格式的特殊字符
	 * @param s
	 * @return
     */
	public static String solveSpecialCharInJson(String s) {
		if( org.apache.commons.lang.StringUtils.isBlank(s) ) {
			return "";
		}
		StringBuffer sb = new StringBuffer ();
		for (int i=0; i<s.length(); i++) {

			char c = s.charAt(i);
			switch (c) {
				case '\"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
//				case '/':
//					sb.append("\\/");
//					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					sb.append(c);
			}
		}
		return sb.toString();
	}
	/**
	 * 是否纯中文字符串
	 * @param cont
	 * @return
	 */
	public static boolean isAbsoluteChineseChar(String cont) {
		if(cont != null) {
			String temp = cont;
			temp = temp.replace("&", "").replace("|", "").replace("(", "").replace(")", "").replaceAll("[\u4E00-\u9FA5\uF900-\uFA2D]", "");
			if(temp.equals("")) {
//				System.out.println("纯汉字");
				return true;
			}
		}
		return false;
	}
	/**
	 * Clob对象转换为字符串
	 * @param clob
	 * @return
	 * @throws Exception
	 */
	public static String ClobToStr(Clob clob) throws Exception{
		String reString = "";
		Reader is = clob.getCharacterStream();// 得到流
		StringReader sr = new StringReader(clob.toString());
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		System.out.println(reString);
		return reString;
	}
	
	/**
	 * 统计指定字符串在另一个字符串中出现的次数
	 */
	public static int occurTimesInStr(String source, String target) {
		StringBuilder tsb = new StringBuilder(target);
		Pattern pattern = Pattern.compile(source);
		Matcher matcher = pattern.matcher(tsb.toString());
		int cnt = 0;
		
		while(matcher.find()) {
			cnt++;
		}
		return cnt;
	}
	
	/**
	 * 统计字符串长度（以UTF-8编码为准，即一个汉字占3个字节，英文和特殊字符占1个
	 */
	public static int calcLen4UTF8(String str) {
		int res = 0;
		int length = str.length();
		for(int i=0; i<length; i++) {
			char c = str.charAt(i);
			String charStr = c + "";
			if(charStr.matches("[\u4E00-\u9FA5\uF900-\uFA2D]")) {
				res += 3;
			} else {
				res += 1;
			}
		}
		return res;
	}

	/**
	 * 从str中截取最大长度为len的字符串（编码UTF8，中文算3个字节
	 * @param str
	 * @param len
     * @return
     */
	public static String cutStringByLen(String str, int len) {
		if( org.apache.commons.lang.StringUtils.isBlank(str) ) {
			return "";
		}
		int realLen = calcLen4UTF8(str);
		if( realLen <= len ) {
			return str;
		} else {
			char[] charArr = str.toCharArray();
			StringBuilder target = new StringBuilder();
			int targetRealSize = 0;
			for(char c : charArr) {
				String charStr = c + "";
				if(charStr.matches("[\u4E00-\u9FA5\uF900-\uFA2D]")) {
					targetRealSize += 3;
				} else {
					targetRealSize += 1;
				}
				if( targetRealSize <= len ) {//当前仅当长度未超过限制时，添加
					target.append(charStr);
				} else {
					break;
				}
			}
			return target.toString();
		}
	}

	public static void main(String[] args) throws InterruptedException {
//		new Thread(()->System.out.println("Hello world!")).start();
//		TimeUnit.SECONDS.sleep(1000);
	}
}
