/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.parser;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cfets.ts.s.platform.bean.ComponentPO;
import com.cfets.ts.s.platform.bean.RelationPO;
import com.cfets.ts.s.platform.exception.PomFileException;
import com.cfets.ddd.d.platform.repository.domain.Component;
import com.cfets.ddd.d.platform.repository.domain.ComponentGroup;
import com.cfets.ddd.d.platform.repository.domain.Relation;
import com.cfets.ddd.d.platform.repository.domain.Scope;
import com.cfets.ts.s.platform.service.ComponentService;
import com.cfets.ts.s.platform.service.impl.ComponentServiceImpl;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年2月27日
 * 
 * @history
 * 
 */
public class PomFileParser extends DefaultHandler {

	private static final Logger logger = Logger.getLogger(PomFileParser.class);
	private ComponentService componentService = new ComponentServiceImpl();
	/**
	 * 所读取的文件
	 */
	private String fileName;

	/**
	 * 父工程
	 */
	private Component parentComponent;

	/**
	 * POM文件工程
	 */
	private Component mainComponent;

	/**
	 * 子工程
	 */
	private Component childComponent;

	/**
	 * 当前依赖关系
	 */
	private Scope scope;

	/**
	 * 依赖的子工程
	 */
	private List<Component> children;

	/**
	 * 依赖关系
	 */
	private List<Relation> relations;

	/**
	 * 进入依赖集合标签之前
	 */
	private boolean isMainTag = true;

	/**
	 * 进入父依赖集合标签
	 */
	private boolean isParentTag = false;

	/**
	 * 进入子依赖集合标签
	 */
	private boolean isChildTag = false;

	/**
	 * 当前元素名称
	 */
	private String currentElement;

	private ComponentGroup group;

	public ComponentGroup readPom(String path, ComponentGroup group) {
		return this.readPom(new File(path), group);
	}

	public ComponentGroup readPom(File file, ComponentGroup group) {
		this.fileName = file.getPath();
		this.group = group;
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(file, this);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace(); // TODO 优化异常处理使在多线程读取比较时不中断，待查证修改
		}
		return this.group;
	}

	private boolean isPom(String qName, Attributes attributes) {
		if ("project".equals(qName)
				&& "http://maven.apache.org/POM/4.0.0".equals(attributes
						.getValue("xmlns"))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void startDocument() throws SAXException {
		try {
			logger.info("开始解析XML文件：" + fileName);
			mainComponent = new Component();// 创建主工程
			children = new ArrayList<Component>();// 创建子工程集合
			relations = new ArrayList<Relation>();// 创建依赖关系集合
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {
			currentElement = qName;
			if (isMainTag) {
				// 是否是pom文件
				if ("project".equals(currentElement)) {
					if (isPom(currentElement, attributes)) {
						logger.info("当前XML文件是POM文件");
					} else {
						throw new PomFileException("当前文件非pom文件");
					}
				}
				if ("parent".equals(currentElement)) {
					isMainTag = false;
					isParentTag = true;
				}
				if ("dependencies".equals(currentElement)) {
					isMainTag = false;
					isChildTag = true;
				}
				return;
			}
			if (isChildTag) {
				// 针对每个依赖建立新对象
				if ("dependency".equals(currentElement)) {
					childComponent = new Component();
					scope = Scope.COMPILE;
				}
			}
			if (isParentTag && parentComponent == null) {
				// 创建父对象
				parentComponent = new Component();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		try {
			String content = new String(ch, start, length).trim();
			if (StringUtils.isBlank(content)) {
				// logger.debug("当前标签[" + currentElement + "]内容为空");
				return;
			}
			if (isMainTag) {
				if ("groupId".equals(currentElement)) {
					mainComponent.setGroupId(content);
				}
				if ("artifactId".equals(currentElement)) {
					mainComponent.setArtifactId(content);
				}
				if ("version".equals(currentElement)) {
					mainComponent.setVersion(content);
				}
				if ("description".equals(currentElement)) {
					mainComponent.setDescription(content);
				}
			}
			if (isParentTag) {
				if ("groupId".equals(currentElement)) {
					parentComponent.setGroupId(content);
					mainComponent.setGroupId(content);// 子继承父的groupId
				}
				if ("artifactId".equals(currentElement)) {
					parentComponent.setArtifactId(content);
				}
				if ("version".equals(currentElement)) {
					parentComponent.setVersion(content);
					mainComponent.setVersion(content);// 子继承父的版本号
				}
			}
			if (isChildTag) {
				if ("groupId".equals(currentElement)) {
					childComponent.setGroupId(content);
				}
				if ("artifactId".equals(currentElement)) {
					childComponent.setArtifactId(content);
				}
				if ("version".equals(currentElement)) {
					childComponent.setVersion(content);
				}
				if ("scope".equals(currentElement)) {
					scope = Scope.obtain(content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {
			currentElement = qName;
			if (isParentTag) {
				if ("parent".equals(currentElement)) {
					isMainTag = true;
					isParentTag = false;
					// 添加依赖关系
					relations.add(Relation.obtain(mainComponent,
							parentComponent, Scope.EXTEND));
				}
			}
			if (isChildTag) {
				if ("dependencies".equals(currentElement)) {
					isMainTag = true;
					isChildTag = false;
				}
				if ("dependency".equals(qName)) {
					children.add(childComponent);
					// 添加依赖关系
					relations.add(Relation.obtain(mainComponent,
							childComponent, scope));
				}
			}
			currentElement = null;// 回收对象
			// scope = null;// 回收对象
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		try {
			logger.info(fileName + "文档读取完毕");
			echo();
			parentComponent = null;
			mainComponent = null;
			childComponent = null;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	private void echo() {
		// 父工程
		logger.info("父构件" + parentComponent.toString());
		logger.info("主构件" + mainComponent.toString());
		for (Component child : children) {
			logger.info("子构件" + child.toString());
		}
		for (Relation relation : relations) {
			logger.info("依赖关系" + relation.toString());
		}
		// group.registerComponent(mainComponent);
		// group.addAllRelation(relations);
		// 这里保存入库:
		ComponentPO parentComepontPO = new ComponentPO();
		if (parentComponent != null
				&& parentComponent.getArtifactId().startsWith("ts")) {
			parentComepontPO.setGroupId(parentComponent.getGroupId());
			parentComepontPO.setArtifactId(parentComponent.getArtifactId());
			parentComepontPO.setVersion(parentComponent.getVersion());
			parentComepontPO.setCreateTime(new Timestamp(System
					.currentTimeMillis()));
			componentService.save(parentComepontPO);
		} else {
			logger.info("不是ts的父构件---"+parentComponent.getArtifactId());
			logger.info("不是ts的父构件---"+parentComponent.getArtifactId().startsWith("ts"));
		}
		ComponentPO mainComponentPO = new ComponentPO();
		mainComponentPO.setGroupId(mainComponent.getGroupId());
		if (StringUtils.isEmpty(mainComponent.getGroupId())) {
			mainComponentPO.setGroupId(parentComponent.getGroupId());
		}
		mainComponentPO.setArtifactId(mainComponent.getArtifactId());
		mainComponentPO.setVersion(mainComponent.getVersion());
		mainComponentPO
				.setCreateTime(new Timestamp(System.currentTimeMillis()));
		if (mainComponentPO.getArtifactId().startsWith("ts")) {
			componentService.save(mainComponentPO);
		}
		else {
			logger.info("不是ts的主构件---");
		}
		for (Component child : children) {
			ComponentPO childComponent = new ComponentPO();
			childComponent.setGroupId(child.getGroupId());
			childComponent.setArtifactId(child.getArtifactId());
			childComponent.setVersion(child.getVersion());
			childComponent.setCreateTime(new Timestamp(System
					.currentTimeMillis()));
			if (childComponent.getArtifactId().startsWith("ts")) {
				componentService.save(childComponent);
			}
			else {
				logger.info("不是ts的构件---"+childComponent.getArtifactId());
			}

		}
		// 保存依赖关系
		for (Relation relation : relations) {
			RelationPO po = new RelationPO();
			Component one = relation.getOne();
			Component another = relation.getAnother();

			Long mainId = componentService.getId(one.getGroupId(),
					one.getArtifactId(), one.getVersion());
			Long anotherId = componentService.getId(another.getGroupId(),
					another.getArtifactId(), another.getVersion());
			
		    logger.info("mainId---"+mainId);
		    logger.info("anotherId---"+anotherId);
			
			if (mainId == null) {
				break;
			}
			if (anotherId == null) {
				continue;
			}
			po.setMainId(mainId);
			po.setDepencyId(anotherId);
			po.setScope(relation.getScope().getName());
			po.setDegree(1);
			componentService.save(po);
		}

	}

}