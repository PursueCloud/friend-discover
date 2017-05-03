package com.yo.friendis.common.ws.interceptor;

import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.EndpointSelectionInterceptor;
import org.apache.cxf.binding.soap.interceptor.ReadHeadersInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class AuthInterceptor extends AbstractSoapInterceptor {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static String HEAD_USERNAME = "username";
	private static String HEAD_PASSWORD = "password";
	private static String auth_user = "yxxt";
	private static String auth_pwd = "yxxt>tag";
	private SAAJInInterceptor saa = new SAAJInInterceptor();

	public AuthInterceptor() {
		super(Phase.UNMARSHAL);
		addAfter(ReadHeadersInterceptor.class.getName());
		addAfter(EndpointSelectionInterceptor.class.getName());
	}

	@SuppressWarnings("unused")
	@Override
	public void handleMessage(SoapMessage message) {
		String userName = null;
		String password = null;
//		需要两种方式获取消息头，应该是格式问题导致
		List<Header> headers1 = message.getHeaders();// 直接获取消息头
		if (!headers1.isEmpty()) {
			for (Header header : headers1) {
				Element e = (Element) header.getObject();
				if (HEAD_USERNAME.equalsIgnoreCase(e.getTagName())) {
					userName = e.getFirstChild().getTextContent();
				} else if (HEAD_PASSWORD.equalsIgnoreCase(e.getTagName())) {
					password = e.getFirstChild().getTextContent();
				}
				if (isNotEmpty(userName, password)) {
					break;
				}
			}
		}
		if (!isNotEmpty(userName, password)) {
			Map<String, List<String>> headers = CastUtils.cast((Map) message.get(Message.PROTOCOL_HEADERS));
			for (String key : headers.keySet()) {
				if (HEAD_USERNAME.equalsIgnoreCase(key)) {
					List<String> values = headers.get(key);
					if (values != null && !values.isEmpty()) {
						userName = values.get(0);
					}
				} else if (HEAD_PASSWORD.equalsIgnoreCase(key)) {
					List<String> values = headers.get(key);
					if (values != null && !values.isEmpty()) {
						password = values.get(0);
					}
				}
				if (isNotEmpty(userName, password)) {
					break;
				}
			}
		}
		if (isNotEmpty(userName, password)) {
			if (checkPermission(userName, password)) {
				return;
			}
		}
		logger.debug("username:{},password:{} .", userName, password);
		SOAPException ex = new SOAPException("认证信息错误！");
		throw new Fault(ex);
	}

	/**
	 * @param userName
	 * @param password
	 * @return 均不为null时返回true
	 */
	private boolean isNotEmpty(String userName, String password) {
		return userName != null && password != null;
	}

	private boolean checkPermission(String userName, String password) {
		if (userName.equals("yhl")) {
			return true;
		}
		return false;
	}

}
