package com.yo.friendis.common.common.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DocUtil {
	Logger logger = LoggerFactory.getLogger(getClass());
	private static DocUtil instance = new DocUtil();
	DocumentBuilderFactory factory;
	DocumentBuilder builder;

	private DocUtil() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error("初始化DocUtil工具类失败");
		}
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static DocUtil getInstance() {
		return instance;
	}

	/**
	 * 把字符串转换为文档
	 * 
	 * @param xml
	 * @return
	 * @throws ParserConfigurationException
	 */
	public Document parseDoc(String xml) throws ParserConfigurationException {
		Document doc = newDoc();
		InputSource source = null;
		StringReader reader = null;
		try {
			builder = factory.newDocumentBuilder();
			reader = new StringReader(xml);
			source = new InputSource(reader);// 使用字符流创建新的输入源
			doc = builder.parse(source);
			return doc;
		} catch (Exception e) {
			return null;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * 创建文档实例
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	public Document newDoc() throws ParserConfigurationException {
		Document doc = builder.newDocument();
		return doc;
	}

	/**
	 * 文档转换为字符串
	 * 
	 * @param doc
	 * @return
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 */
	public String xmlToString(Document doc) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
		StringWriter output = new StringWriter();
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(output));
		return output.toString();
	}

	/**
	 * 创建操作信息响应xml
	 * 
	 * @param success
	 * @param code
	 * @param message
	 * @return 创建失败返回null
	 */
	public String newOperatResp(boolean success,String code, String message) {
		try {
			Document doc = newDoc();
			Element dataEle = doc.createElement("data");
			Element successEle = doc.createElement("success");
			successEle.appendChild(doc.createTextNode(String.valueOf(success)));
			Element codeEle = doc.createElement("code");
			codeEle.appendChild(doc.createTextNode(code));
			Element msgEle = doc.createElement("message");
			msgEle.appendChild(doc.createTextNode(message));

			dataEle.appendChild(successEle);
			dataEle.appendChild(codeEle);
			dataEle.appendChild(msgEle);
			doc.appendChild(dataEle);
			return xmlToString(doc);

		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage());
		} catch (TransformerConfigurationException e) {
			logger.error(e.getMessage());
		} catch (TransformerException e) {
			logger.error(e.getMessage());
		} catch (TransformerFactoryConfigurationError e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * @param doc
	 * @param name
	 * @return null
	 */
	public String getParameterByName(Document doc, String name) {
		Node data = doc.getElementsByTagName("data").item(0);
		NodeList nodes = data.getOwnerDocument().getElementsByTagName(name);
		if (nodes.getLength() > 0) {
			return nodes.item(0).getTextContent();
		}
		return null;
	}

	/**
	 * @param doc
	 * @param parent
	 *            if parent is null,add this element to doc
	 * @param name
	 * @param value
	 *            if vlaue is null,just add element without content
	 * @throws NoParentNodeException
	 */
	public void addElement(Document doc, String parent, String name, Object value) throws NoParentNodeException {
		Node newEle = doc.createElement(name);
		if (value != null) {
			newEle.appendChild(doc.createTextNode(String.valueOf(value)));
		}
		if (parent == null) {
			doc.appendChild(newEle);
		} else {
			Node pNode = doc.getElementsByTagName(parent).item(0);
			if (pNode != null) {
				pNode.appendChild(newEle);
			} else {
				throw new NoParentNodeException();
			}
		}

	}

	/**
	 * 添加子节点
	 * @param doc
	 * @param parent
	 * @param children
	 * @throws NoParentNodeException
	 */
	public void addElement(Document doc, String parent, Element children) throws NoParentNodeException {
		Node pNode = doc.getElementsByTagName(parent).item(0);
		if (pNode != null) {
			pNode.appendChild(children);
		} else {
			throw new NoParentNodeException();
		}
	}

	public class NoParentNodeException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3443636352306205177L;

		public NoParentNodeException() {
			super("没有父节点");
		}
	}
}
