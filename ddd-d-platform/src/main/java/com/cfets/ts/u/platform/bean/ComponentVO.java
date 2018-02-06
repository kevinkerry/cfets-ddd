/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.u.platform.bean;

import java.sql.Timestamp;

import com.cfets.cwap.s.util.annotation.Note;
import com.cfets.cwap.s.util.db.Table;
import com.cfets.cwap.s.util.db.TableColumn;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年3月6日
 * 
 * @history
 * 
 */

//@Table(name = "TSBASE.MAVEN_DATA")
public class ComponentVO implements java.io.Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3326928416150419968L;
	@Note(note = "主键")
	//@TableColumn(name = "SR_NO_ID", auto = true, sequence = "TSBASE.SEQ_MAVEN_DATA")
	private Long id;
	// 群组名称
	//@TableColumn(name = "GRP_NM")
	private String groupId;
	// 文件名称
	//@TableColumn(name = "FILE_NM")
	private String artifactId;
	// 业务组件版本
	//@TableColumn(name = "WDGT_VRSN")
	private String version;
	// name
	//@TableColumn(name = "USR_NM")
	private String name;
	// 元素描述
	//@TableColumn(name = "MTDT_DESC")
	private String description;

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	//@TableColumn(name = "EN_CNTNT_LINK_ADRS")
	private String url;

	//@TableColumn(name = "ISR_EN_SHRT_NM")
	private String developer;

	@Note(note = "创建时间")
	//@TableColumn(name = "CRT_TM")
	private Timestamp createTime;

	@Note(note = "修改时间")
	//@TableColumn(name = "UPD_TM")
	private Timestamp updateTime;

	@Note(note = "创建人")
	//@TableColumn(name = "CRTR")
	private String makeUser;

	@Note(note = "修改人")
	//@TableColumn(name = "UPDTR")
	private String updateUser;
	
	//@TableColumn(name = "THE_MKT_DPTH_ID")
	private int degree;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getMakeUser() {
		return makeUser;
	}

	public void setMakeUser(String makeUser) {
		this.makeUser = makeUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
