package com.yo.friendis.common.common.util;

import java.io.StringWriter;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.yo.friendis.common.common.bean.OperaterResult;

public class WebServiceProxy {
	// 名字空间
	private String targetNamespace = "http://webservice.brilliant-solution.com/";
	// 服务名
	private String serviceName = "SsoWebService";
	// 端口名
	private String portName = "SsoWebService";
	// 服务地址
	private String endpointAddress = "http://localhost:8892/ws/SsoWebService?wsdl";

	private WebServiceProxy() {
		super();
	};

	public WebServiceProxy(String endpointAddress, String targetNamespace, String servicName, String portName) {
		this();
		this.targetNamespace = targetNamespace;
		this.serviceName = servicName;
		this.portName = portName;
		this.endpointAddress = endpointAddress;
	}

	public OperaterResult<String> invoke(String methodName, String[] headers, String[] headerValues, String[] args, String[] values) throws SOAPException {
		QName serviceQName = new QName(targetNamespace, serviceName);

		QName portQName = new QName(targetNamespace, portName);

		Service service = Service.create(serviceQName);
		service.addPort(portQName, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);

		Dispatch<SOAPMessage> dispatch = service.createDispatch(portQName, SOAPMessage.class, Service.Mode.MESSAGE);

		BindingProvider bp = (BindingProvider) dispatch;
		Map<String, Object> rc = bp.getRequestContext();
		rc.put(BindingProvider.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
		rc.put(BindingProvider.SOAPACTION_URI_PROPERTY, methodName);
		MessageFactory factory = ((SOAPBinding) bp.getBinding()).getMessageFactory();
		SOAPMessage request = factory.createMessage();
		if (headers != null && headerValues != null && headers.length == headerValues.length) {
			SOAPHeader header = request.getSOAPHeader();
			for (int i = 0; i < headers.length; i++) {
				SOAPHeaderElement headEle = header.addHeaderElement(new QName(targetNamespace, headers[i]));
				headEle.addTextNode(headerValues[i]);
			}
		}
		SOAPBody body = request.getSOAPBody();
		QName methodQName = new QName(targetNamespace, methodName, "ns1");
		SOAPBodyElement bodyElement = body.addBodyElement(methodQName);

		if (args != null && values != null && args.length == values.length) {
			for (int i = 0; i < args.length; i++) {
				SOAPElement arg = bodyElement.addChildElement(args[i]);
				arg.addTextNode(values[i]);
			}
		} else {
			return new OperaterResult<String>(false, "参数错误");
		}

		SOAPMessage resMsg = null;
		try {
			resMsg = dispatch.invoke(request);
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error("调用{}方法时出错", methodName);
			LoggerFactory.getLogger(this.getClass()).error(e.getLocalizedMessage());
			return new OperaterResult<String>(false, "调用方法时出错");
		}
		SOAPBody soapBody = resMsg.getSOAPBody();
		SOAPBodyElement nextSoapBodyElement = (SOAPBodyElement) soapBody.getChildElements().next();
		SOAPElement soapElement = (SOAPElement) nextSoapBodyElement.getChildElements().next();

		try {
			// 把SOAPMessage转换为string
			Document doc = resMsg.getSOAPPart().getEnvelope().getOwnerDocument();
			StringWriter output = new StringWriter();
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));
			LoggerFactory.getLogger(this.getClass()).debug("调用{}方法,返回结果：{}", methodName, output.toString());
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).error(e.getLocalizedMessage());
		}

		return new OperaterResult<String>(true, "",soapElement.getValue());
	}

	public OperaterResult<String> invoke(String methodName, String[] args, String[] values) throws SOAPException {
		return invoke(methodName, null, null, args, values);
	}

}
