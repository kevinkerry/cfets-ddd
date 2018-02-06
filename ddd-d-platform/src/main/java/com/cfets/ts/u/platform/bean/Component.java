/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.u.platform.bean;

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
public class Component implements java.io.Serializable {

	private static final long serialVersionUID = 8452585157017445269L;

	private String groupId;

	private String artifactId;

	private String version;

	private String description;
	


	private int deep;

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

	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}

	public String getName() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(groupId);
		sb.append("]");
		sb.append("[");
		sb.append(artifactId);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(groupId);
		sb.append("]");
		sb.append("[");
		sb.append(artifactId);
		sb.append("]");
		sb.append("[");
		sb.append(version);
		sb.append("]");
		return sb.toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



}
