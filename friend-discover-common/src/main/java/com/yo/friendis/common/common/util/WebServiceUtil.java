package com.yo.friendis.common.common.util;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(WebServiceUtil.class);

	private static int CONN_TIMEOUT = 3 * 1000;// 毫秒
	private static int RESP_TIMEOUT = 1 * 1000;// 毫秒

	/**
	 * @param wsdl 服务地址
	 * @param targetNamespace 命名空间
	 * @param serviceName 服务名称
	 * @param portName 服务端口名称
	 * @param opName 方法名称
	 * @param xml 参数字符串，以xml格式
	 * @return
	 * 
	 * @author yhl
	 * @Description 调用webservice服务
	 * @date 2017年1月18日 上午10:56:59
	 */
	public static String invoke(String wsdl, String targetNamespace, String serviceName, String portName, String opName, String xml) {
		try {
			return _invoke(wsdl, targetNamespace, serviceName, portName, opName, xml);
		} catch (Exception e) {
			LOGGER.error("调用服务资源出错，casuse:{}", e.getMessage());
			return "";
		}
	}

	private static String _invoke(String wsdl, String targetNamespace, String serviceName, String portName, String methodName, String xml) throws SOAPException {
		QName serviceQName = new QName(targetNamespace, serviceName);

		QName portQName = new QName(targetNamespace, portName);

		Service service = Service.create(serviceQName);
		service.addPort(portQName, SOAPBinding.SOAP11HTTP_BINDING, wsdl);

		Dispatch<SOAPMessage> dispatch = service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE);
		BindingProvider bp = (BindingProvider) dispatch;
		Map<String, Object> rc = bp.getRequestContext();
		rc.put(BindingProvider.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
		rc.put(BindingProvider.SOAPACTION_URI_PROPERTY, methodName);
		rc.put("javax.xml.ws.client.connectionTimeout", CONN_TIMEOUT);
		rc.put("javax.xml.ws.client.receiveTimeout", RESP_TIMEOUT);
		MessageFactory factory = ((SOAPBinding) bp.getBinding()).getMessageFactory();
		SOAPMessage request = factory.createMessage();
		SOAPBody body = request.getSOAPBody();
		QName methodQName = new QName(targetNamespace, methodName, "ns1");
		SOAPBodyElement bodyElement = body.addBodyElement(methodQName);

		SOAPElement arg = bodyElement.addChildElement("arg0");
		arg.addTextNode(xml);

		SOAPMessage resMsg = null;
		resMsg = dispatch.invoke(request);
		SOAPBody soapBody = resMsg.getSOAPBody();
		SOAPBodyElement nextSoapBodyElement = (SOAPBodyElement) soapBody.getChildElements().next();
		SOAPElement soapElement = (SOAPElement) nextSoapBodyElement.getChildElements().next();
		return soapElement.getValue();
	}
}
