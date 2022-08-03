package com.ay.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * dom4j + xpath 解析 xml
 * 
 * @author jackson
 *
 */
public class XmlUtil {

	private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	private SAXReader reader;

	private Document document;

	/**
	 * sample : /result/@info <===> <result info="">
	 */
	@SuppressWarnings("unchecked")
	public List<Attribute> getAttribute(String xpath) {
		if (reader == null) {
			throw new NullPointerException("SAXReader");
		}
		return document.selectNodes(xpath);
	}

	/**
	 * sample : /response/errcode <===>
	 * <response><errcode>12321</errcode></response>
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getSelectNodes(String xpath) {
		if (reader == null) {
			throw new NullPointerException("SAXReader");
		}
		return document.selectNodes(xpath);
	}

	public XmlUtil(String xml) {
		reader = new SAXReader();
		try {
			document = reader.read(new BufferedInputStream(new ByteArrayInputStream(xml.trim().getBytes())));
		} catch (DocumentException e) {
			logger.info("解析xml异常 : ", e);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			String s = "<root><row dataType=\"BR\"  billNo=\"190412133242332\" playerName=\"krnz19911022\" agentCode=\"AJ0001001001001\" gameCode=\"GC006194120QY\" netAmount=\"-33800\" betTime=\"2019-04-12 13:14:07\" gameType=\"BAC\" betAmount=\"33800\" validBetAmount=\"33800\" flag=\"1\" playType=\"2\" currency=\"CNY\" tableCode=\"C006\" loginIP=\"36.61.74.163\" recalcuTime=\"2019-04-12 13:14:33\" platformType=\"AGIN\" remark=\"\" round=\"DSP\" result=\"\" beforeCredit=\"138855.25\" deviceType=\"1\" /><row dataType=\"BR\"  billNo=\"190412133250824\" playerName=\"krnhongmei\" agentCode=\"AJ0001001001001\" gameCode=\"GB002194120SV\" netAmount=\"-303\" betTime=\"2019-04-12 13:14:19\" gameType=\"BAC\" betAmount=\"303\" validBetAmount=\"303\" flag=\"1\" playType=\"1\" currency=\"CNY\" tableCode=\"B002\" loginIP=\"39.191.206.199\" recalcuTime=\"2019-04-12 13:14:41\" platformType=\"AGIN\" remark=\"\" round=\"AGQ\" result=\"\" beforeCredit=\"303.95\" deviceType=\"1\" /></root>";
			XmlUtil xmlUtil = new XmlUtil(s);
//			List<Attribute> attribute1 = xmlUtil.getAttribute("/row");
			List<Element> nodes = xmlUtil.getSelectNodes("/root/row");
			for (Element element : nodes) {
				System.out.println(element.attributeValue("dataType"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
