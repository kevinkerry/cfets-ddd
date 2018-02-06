package com.cfets.ts.u.platform.bean;

import java.util.Date;

public class MegerRequestVO implements Comparable<MegerRequestVO> {
	private static final long serialVersionUID = -5229572635650073364L;
	private Long id;
	// 提交人姓名
	private String authorName;
	// 提交时间
	private Date requestTimeT;
	// merge请求状态
	private String status;
	// merge描述
	private String description;
	// 构件名
	private String projectName;
	private String version;// 版本号
	private String requestTime;// 发布时间
	private Integer productId;
	private String title;
	private String relationId;// 相关mergeID
	private String isVersion;
	private String mergeid;

	public Date getRequestTimeT() {
		return requestTimeT;
	}

	public void setRequestTimeT(Date requestTimeT) {
		this.requestTimeT = requestTimeT;
	}

	public String getMergeid() {
		return mergeid;
	}

	public void setMergeid(String mergeid) {
		this.mergeid = mergeid;
	}

	public String getIsVersion() {
		return isVersion;
	}

	public void setIsVersion(String isVersion) {
		this.isVersion = isVersion;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	// 目标分支
	private String branch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/*
	 * public Date getRequestTime() { return requestTime; } public void
	 * setRequestTime(Date requestTime) { this.requestTime = requestTime; }
	 */

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public int compareTo(MegerRequestVO o) {
		if (o.getRequestTimeT().getTime() > this.getRequestTimeT().getTime()) {
			return -1;
		} else if (o.getRequestTimeT().getTime() < this.getRequestTimeT()
				.getTime()) {
			return 1;
		}
		return 0;
	}

}
