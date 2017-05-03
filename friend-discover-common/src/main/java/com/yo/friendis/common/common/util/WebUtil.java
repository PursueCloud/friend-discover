package com.yo.friendis.common.common.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.yo.friendis.common.common.Common;

public class WebUtil {
	
	
	/**
	 * 默认编码字符：UTF-8
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 默认返回数据格式：json
	 */
	public static final String CTYPE_JSON = "application/json";
	
	
	/**
	 * 返回数据格式：html
	 */
	public static final String CTYPE_HTML= "text/html";
	
	
	/**
	 * 网络接口回调
	 * 
	 * @author walidake
	 *
	 */
	public interface Callback{
		public void onSuccess(HttpEntity entity);
		public void onFailed();
	}
	
	
	
	public static String getRequestRootPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
	}

	/**
	 * 获取文件服务地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getFileServerPath(HttpServletRequest request) {
		String url = ConfigServiceUtil.getConfigValue(Common.DBConfigKeys.FILE_SERVER_URL.toString());
		if (url == null) {
			return WebUtil.getRequestRootPath(request);
		}
		if (!StringUtils.startsWith(url, "http")) {
			url = StringUtils.removeEnd(url, "/");
			url = WebUtil.getRequestRootPath(request) + url;
		}
		if (!StringUtils.endsWith(url, "/")) {
			url += "/";
		}
		return url;
	}
	
	
	/**
	 * 判断是否是ajax请求（仅限于jquery ajax的情况）
	 * 
	 * 		低版本的jquery会自动加上头部，而高版本的需要手动设置
	 * 
	 * @param request
	 * @return
	 */
	@Deprecated
	public static boolean isAjaxRequest(HttpServletRequest request) { 
		String header = request.getHeader("X-Requested-With");
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			System.out.println(headerNames.nextElement() + ",");
		}
	    return "XMLHttpRequest".equals(header);
	}
	
	/**
	 * 向前端页面写回ajax返回数据
	 * 
	 * @param response
	 * @param state
	 * @return
	 */
	public static <T> void writeAjaxRet(HttpServletResponse resp, T data) throws IOException{
		// 解决乱码
		resp.setCharacterEncoding(DEFAULT_CHARSET);
		resp.setContentType(CTYPE_HTML);

		// 如果为json格式，格式化数据
		String jsonString = JSON.toJSONString(data);
		resp.getOutputStream().write(jsonString.getBytes(DEFAULT_CHARSET));
		//貌似自动关闭？
		resp.getOutputStream().close();
	}
	
	
	/**
	 * 获取本地完整地址【即保存路径地址】
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocalFullPath(HttpServletRequest request) {
		return request.getRealPath("/");
	}
	
	/**
	 * 获取服务器完整地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getServerFullPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";
	}
	
	
	/**
	 * 发起简单post请求
	 * 
	 * @param path
	 * @param callback
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void sendSimplePostRequest(String path,Callback callback) throws ClientProtocolException, IOException{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(path);
		HttpResponse response = httpClient.execute(httpPost);// 执行提交
		HttpEntity responseEntity = response.getEntity();
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			callback.onSuccess(responseEntity);
		}
		callback.onFailed();
	}
	
}
