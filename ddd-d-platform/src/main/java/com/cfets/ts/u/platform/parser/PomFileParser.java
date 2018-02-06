/*<<<<<<< HEAD
 *//**
 * 辅助交易系统ts-s-platform
 */
/*
 package com.cfets.ts.u.platform.parser;

 import java.io.ByteArrayInputStream;
 import java.io.FileInputStream;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.List;

 import javax.sql.DataSource;
 import javax.xml.parsers.SAXParser;
 import javax.xml.parsers.SAXParserFactory;

 import org.apache.commons.lang.StringUtils;
 import org.apache.log4j.Logger;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.xml.sax.Attributes;
 import org.xml.sax.SAXException;
 import org.xml.sax.helpers.DefaultHandler;

 import com.cfets.ts.u.platform.bean.Component;
 import com.cfets.ts.u.platform.bean.ComponentGroup;
 import com.cfets.ts.u.platform.bean.ComponentPO;
 import com.cfets.ts.u.platform.bean.Relation;
 import com.cfets.ts.u.platform.bean.RelationPO;
 import com.cfets.ts.u.platform.bean.Scope;
 import com.cfets.ts.u.platform.service.ComponentService;
 import com.cfets.ts.u.platform.service.ComponentServiceImpl;
 import com.github.abel533.echarts.Data;

 *//**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年2月27日
 * 
 * @history
 * 
 */
/*
 @org.springframework.stereotype.Component
 public class PomFileParser extends DefaultHandler {
 private static final Logger logger = Logger.getLogger(PomFileParser.class);
 @Autowired
 private ComponentService componentService;
 *//**
 * 所读取的文件
 */
/*
 private String fileName;

 *//**
 * 父工程
 */
/*
 private Component parentComponent;

 *//**
 * POM文件工程
 */
/*
 private Component mainComponent;

 *//**
 * 子工程
 */
/*
 private Component childComponent;

 *//**
 * 当前依赖关系
 */
/*
 private Scope scope;

 *//**
 * 依赖的子工程
 */
/*
 private List<Component> children;

 *//**
 * 依赖关系
 */
/*
 private List<Relation> relations;

 *//**
 * 进入依赖集合标签之前
 */
/*
 private boolean isMainTag = true;

 *//**
 * 进入父依赖集合标签
 */
/*
 private boolean isParentTag = false;

 *//**
 * 进入子依赖集合标签
 */
/*
 private boolean isChildTag = false;

 *//**
 * 当前元素名称
 */
/*
 private String currentElement;

 private ComponentGroup group;
 private String branchType;

 public ComponentGroup readPom(String file, ComponentGroup group,
 String branchType) {
 this.group = group;
 this.branchType = branchType;
 try {

 ByteArrayInputStream in = new ByteArrayInputStream(
 file.getBytes("utf-8"));
 SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
 saxParser.parse(in, this);
 } catch (Exception e) {
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

 // 节点解析开始调用
 *//**
 * @param url
 *            命名空间的url 标签的名称 待命名空间的标签名称
 */
/*
 @Override
 public void startElement(String uri, String localName, String qName,
 Attributes attributes) throws SAXException {
 logger.info("开始解析节点：" + qName);
 try {
 currentElement = qName;
 if (isMainTag) {
 // 是否是pom文件
 if ("project".equals(currentElement)) {
 if (isPom(currentElement, attributes)) {
 logger.info("当前XML文件是POM文件");
 } else {
 throw new Exception("当前文件非pom文件");
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
 if ("build".equals(currentElement)
 || "reporting".equals(currentElement)) {
 isMainTag = false;
 isChildTag = false;
 isParentTag = false;
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
 logger.debug("当前标签[" + currentElement + "]内容为空");
 return;
 }
 logger.info("开始解析content：" + content);
 if (isMainTag) {
 if ("groupId".equals(currentElement)) {
 mainComponent.setGroupId(content);
 }
 if ("artifactId".equals(currentElement)) {

 * if("maven-project-info-reports-plugin".equals(content)){
 * System.out.println("&&&&&&&&&&&&&"); }

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
 // scope = Scope.obtain(content);
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
 logger.info("结束解析节点：" + qName);
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
 } else if ("build".endsWith(qName) || "reporting".equals(qName)) {
 isMainTag = true;
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

 * ComponentPO parentComepontPO = new ComponentPO(); if (parentComponent
 * != null && parentComponent.getArtifactId().startsWith("ts")) {
 * parentComepontPO.setGroupId(parentComponent.getGroupId());
 * parentComepontPO.setArtifactId(parentComponent.getArtifactId());
 * parentComepontPO.setVersion(parentComponent.getVersion());
 * parentComepontPO.setCreateTime(new Timestamp(System
 * .currentTimeMillis())); componentService.save(parentComepontPO); }
 * else { logger.info("不是ts的父构件---"+parentComponent.getArtifactId());
 * logger
 * .info("不是ts的父构件---"+parentComponent.getArtifactId().startsWith("ts"
 * )); }

 ComponentPO mainComponentPO = new ComponentPO();
 mainComponentPO.setGroupId(mainComponent.getGroupId());
 if (StringUtils.isEmpty(mainComponent.getGroupId())) {
 mainComponentPO.setGroupId(parentComponent.getGroupId());
 }
 mainComponentPO.setArtifactId(mainComponent.getArtifactId());
 mainComponentPO.setVersion(mainComponent.getVersion());
 mainComponentPO
 .setCreateTime(new Timestamp(System.currentTimeMillis()));
 mainComponentPO.setPomType(this.branchType);
 // if (mainComponentPO.getArtifactId().startsWith("ts")) {
 //componentService.save(mainComponentPO);

 // 保存依赖关系

 for (Relation relation : relations) {
 RelationPO po = new RelationPO();
 Component one = relation.getOne();
 Component another = relation.getAnother();
 // one.getVersion()是无用的
 Long mainId = componentService.getId(one.getGroupId(),
 one.getArtifactId(), this.branchType);
 Long anotherId = componentService.getId(another.getGroupId(),
 another.getArtifactId(), this.branchType);

 if (mainId == null) {
 break;
 }
 if (anotherId == 0 || anotherId == null) {
 // 保存
 ComponentPO depencyPO = new ComponentPO();
 depencyPO.setGroupId(another.getGroupId());
 depencyPO.setArtifactId(another.getArtifactId());
 depencyPO.setVersion(another.getVersion());
 depencyPO.setPomType(this.branchType);
 componentService.save(depencyPO);
 anotherId = componentService.getId(another.getGroupId(),
 another.getArtifactId(), another.getVersion());
 }
 po.setMainId(mainId);
 po.setDepencyId(anotherId);
 po.setScope(relation.getScope().getName());
 po.setDegree(1);
 componentService.save(po);
 }

 }

 =======*/
/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.u.platform.parser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cfets.ts.u.platform.bean.Component;
import com.cfets.ts.u.platform.bean.ComponentGroup;
import com.cfets.ts.u.platform.bean.ComponentPO;
import com.cfets.ts.u.platform.bean.Relation;
import com.cfets.ts.u.platform.bean.RelationPO;
import com.cfets.ts.u.platform.bean.Scope;
import com.cfets.ts.u.platform.service.ComponentService;
import com.cfets.ts.u.platform.service.ComponentServiceImpl;

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
@org.springframework.stereotype.Component
public class PomFileParser extends DefaultHandler {
	private static final Logger logger = Logger.getLogger(PomFileParser.class);
	@Autowired
	private ComponentService componentService;
	@Autowired
	ComponentPO mainComponentPO;
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
	private String branchType;

	public ComponentGroup readPom(String file, ComponentGroup group,
			String branchType) {
		this.group = group;
		this.branchType = branchType;
		try {

			ByteArrayInputStream in = new ByteArrayInputStream(
					file.getBytes("utf-8"));
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(in, this);
		} catch (Exception e) {
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

	// 节点解析开始调用
	/**
	 * @param url
	 *            命名空间的url 标签的名称 待命名空间的标签名称
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		logger.info("开始解析节点：" + qName);
		try {
			currentElement = qName;
			if (isMainTag) {
				// 是否是pom文件
				if ("project".equals(currentElement)) {
					if (isPom(currentElement, attributes)) {
						logger.info("当前XML文件是POM文件");
					} else {
						throw new Exception("当前文件非pom文件");
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
				if ("build".equals(currentElement)
						|| "reporting".equals(currentElement)) {
					isMainTag = false;
					isChildTag = false;
					isParentTag = false;
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

	String pexMaxLengthChar = "";

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		try {
			String content = new String(ch, start, length).trim();
			// 如果长度超过2048要特别处理,否则截取的数据不对，com.c
			if (start + length == 2048) {
				pexMaxLengthChar = content;
				return;
			}
			if (start == 0) {
				content = pexMaxLengthChar + content;
			}

			if (StringUtils.isBlank(content)) {
				logger.debug("当前标签[" + currentElement + "]内容为空");
				return;
			}
			logger.info("开始解析content：" + content);
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
		//logger.info("结束解析节点：" + qName);
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
			} else if ("build".endsWith(qName) || "reporting".equals(qName)) {
				isMainTag = true;
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
		// 这里保存父构件库:
		ComponentPO parentComepontPO = new ComponentPO();
		if (parentComponent != null
				&&"com.cfets.ts".equals(parentComponent.getGroupId()) ) {
			parentComepontPO.setGroupId(parentComponent.getGroupId());
			parentComepontPO.setArtifactId(parentComponent.getArtifactId());
			parentComepontPO.setVersion(parentComponent.getVersion());
			parentComepontPO.setCreateTime(new Timestamp(System
					.currentTimeMillis()));
			parentComepontPO.setPomType(this.branchType);
			componentService.save(parentComepontPO);
		} else {
			logger.info("不是ts的父构件---"
					+ parentComponent.getGroupId());
		}

		mainComponentPO.setGroupId(mainComponent.getGroupId());
		if (StringUtils.isEmpty(mainComponent.getGroupId())) {
			mainComponentPO.setGroupId(parentComponent.getGroupId());
		}
		mainComponentPO.setArtifactId(mainComponent.getArtifactId());
		mainComponentPO.setVersion(mainComponent.getVersion());
		mainComponentPO
				.setCreateTime(new Timestamp(System.currentTimeMillis()));

		mainComponentPO.setPomType(this.branchType);
		// if (mainComponentPO.getArtifactId().startsWith("ts")) {
		if(mainComponentPO.getArtifactId().isEmpty()||mainComponentPO.getVersion().isEmpty()){
			logger.info("getArtifactId==null");
			return;
		}
		componentService.save(mainComponentPO);

		// 保存子构件
		for (Component child : children) {
			ComponentPO childComponent = new ComponentPO();
			childComponent.setGroupId(child.getGroupId());
			childComponent.setArtifactId(child.getArtifactId());
			childComponent.setVersion(child.getVersion());
			childComponent.setCreateTime(new Timestamp(System
					.currentTimeMillis()));
			childComponent.setPomType(this.branchType);
			if ("com.cfets.ts".equals(childComponent.getGroupId())) {
				if(childComponent.getArtifactId().isEmpty()||childComponent.getVersion().isEmpty()){
					continue;
				}
				componentService.save(childComponent);
			} else {
				//logger.info("不是ts的构件---" + childComponent.getArtifactId());
			}

		}
		// 保存依赖关系

		for (Relation relation : relations) {
			RelationPO po = new RelationPO();
			Component one = relation.getOne();
			Component another = relation.getAnother();
			// one.getVersion()是无用的
			Long mainId = componentService.getId(one.getGroupId(),
					one.getArtifactId(), this.branchType);
			Long anotherId = componentService.getId(another.getGroupId(),
					another.getArtifactId(), this.branchType);

			if (mainId == null) {
				break;
			}
			if (anotherId == 0L || anotherId == null) {
				// 保存
				logger.info("不是ts的构件，不保存在关系中---" + another);
				continue;
				/*
				 * ComponentPO depencyPO = new ComponentPO();
				 * depencyPO.setGroupId(another.getGroupId());
				 * depencyPO.setArtifactId(another.getArtifactId());
				 * depencyPO.setVersion(another.getVersion());
				 * depencyPO.setPomType(this.branchType);
				 * componentService.save(depencyPO); anotherId =
				 * componentService.getId(another.getGroupId(),
				 * another.getArtifactId(), depencyPO.getPomType());
				 */
			}
			po.setMainId(mainId);
			po.setDepencyId(anotherId);
			po.setScope(relation.getScope().getName());
			po.setDegree(1);
			po.setVersion(another.getVersion());
			componentService.save(po);
		}

	}
}
