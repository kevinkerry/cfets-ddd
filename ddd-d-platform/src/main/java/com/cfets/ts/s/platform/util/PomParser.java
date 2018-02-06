package com.cfets.ts.s.platform.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.cfets.ts.s.platform.bean.DependencyBean;
import com.cfets.ts.s.platform.bean.ElementBean;

public class PomParser extends DefaultHandler {

	private String fileName;

	private DependencyBean dependencyBean = null;

	private String currentElement;

	private boolean isEnteredDependencyTag = false;

	private Map<DependencyBean, String> dependencyBeans;

	private ElementBean elementBean;

	private boolean isBeforeDependencyTag = true;

	private List<ElementBean> beforeDependencyBeans;

	private boolean isAfterDependencyTag = false;

	private List<ElementBean> afterDependencyBeans;

	public void readPom(String path) {
		this.fileName = path;
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(new File(path), this);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace(); // TODO 优化异常处理使在多线程读取比较时不中断，待查证修改
		}
	}

	public void readPom(File file) {
		this.fileName = file.getPath();
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(file, this);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace(); // TODO 优化异常处理使在多线程读取比较时不中断，待查证修改
		}
	}

	public Map<DependencyBean, String> getDependencyBeanMap() {
		return dependencyBeans;
	}

	public List<ElementBean> getBeforeDependencyBeans() {
		return beforeDependencyBeans;
	}

	public List<ElementBean> getAfterDependencyBeans() {
		return afterDependencyBeans;
	}

	@Override
	public void startDocument() throws SAXException {
		System.out.println("开始读取文档：" + fileName);
		dependencyBeans = new LinkedHashMap<DependencyBean, String>();
		beforeDependencyBeans = new ArrayList<ElementBean>();
		afterDependencyBeans = new ArrayList<ElementBean>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElement = qName;
		System.out.println("start-->" + qName);
		if (isBeforeDependencyTag) {
			elementBean = new ElementBean(qName,
					new AttributesImpl(attributes), ElementBean.START_ELEMENT);
			beforeDependencyBeans.add(elementBean);
			if ("dependencies".equals(qName)) {
				isBeforeDependencyTag = false;
			}
			return;
		}
		if (isAfterDependencyTag) {
			elementBean = new ElementBean(qName,
					new AttributesImpl(attributes), ElementBean.START_ELEMENT);
			afterDependencyBeans.add(elementBean);
			return;
		}
		if ("dependency".equals(qName)) {
			dependencyBean = new DependencyBean();
			isEnteredDependencyTag = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentElement = null;
		System.out.println("end-->" + qName);
		if (isBeforeDependencyTag) {
			elementBean = new ElementBean(qName, null, ElementBean.END_ELEMENT);
			beforeDependencyBeans.add(elementBean);
			return;
		}
		if (isAfterDependencyTag) {
			elementBean = new ElementBean(qName, null, ElementBean.END_ELEMENT);
			afterDependencyBeans.add(elementBean);
			return;
		}
		if ("dependency".equals(qName)) {
			dependencyBeans.put(dependencyBean, dependencyBean.getVersion());
			isEnteredDependencyTag = false;
			return;
		}
		if ("dependencies".equals(qName)) {
			elementBean = new ElementBean(qName, null, ElementBean.END_ELEMENT);
			afterDependencyBeans.add(elementBean);
			isAfterDependencyTag = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// System.out.println("char-->"+String.valueOf(ch));
		String content = new String(ch, start, length).trim();
		boolean isNotBlank = !"".equals(content);
		if (isNotBlank) {
			if (isBeforeDependencyTag) {
				elementBean = new ElementBean(content, null,
						ElementBean.CHARACTERS);
				beforeDependencyBeans.add(elementBean);
				return;
			}
			if (isAfterDependencyTag) {
				elementBean = new ElementBean(content, null,
						ElementBean.CHARACTERS);
				afterDependencyBeans.add(elementBean);
				return;
			}
		}
		if (isEnteredDependencyTag) {
			if ("groupId".equals(currentElement)) {
				dependencyBean.setGroupId(content);
				return;
			}
			if ("artifactId".equals(currentElement)) {
				dependencyBean.setArtifactId(content);
				return;
			}
			if ("version".equals(currentElement)) {
				dependencyBean.setVersion(content);
				return;
			}
			if ("scope".equals(currentElement)) {
				dependencyBean.setScope(content);
				return;
			}
		}
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println(fileName + "-> 文档读取完毕");
		isEnteredDependencyTag = false;
		isBeforeDependencyTag = true;
		isAfterDependencyTag = false;
	}
}
