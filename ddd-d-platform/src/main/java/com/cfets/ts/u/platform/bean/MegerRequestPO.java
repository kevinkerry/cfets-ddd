package com.cfets.ts.u.platform.bean;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.cfets.cwap.s.util.annotation.Note;
import com.cfets.cwap.s.util.db.Table;
import com.cfets.cwap.s.util.db.TableColumn;

@Component
@Table(name = "TSBASE.MERGE_REQUEST")
public class MegerRequestPO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2984416393103951841L;
	@TableColumn(name = "SR_NO_ID", auto = true, sequence = "TSBASE.SEQ_MERGE_REQUEST")
	@Note(note = "主键")
	private Long id;
	// 提交人姓名
	private String authorName;
	// mergeId
	private String mergeid;
	// 提交时间
	private Date requestTime;
	// merge请求状态 0，发布失败;1,发布成功，2，未发布，4，打包成功，5，打包失败，6，未打包
	
	private String status;
	// merge描述
	private String description;

	private String title;
	private String version;// 版本号
	private String isVersion;// 是否要发版,0,要发版，
	private String relationId;// 关联的releaseid
	private Integer projectId;



	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getIsVersion() {
		return isVersion;
	}

	public void setIsVersion(String isVersion) {
		this.isVersion = isVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// 构件名
	@TableColumn(name = "project_name")
	private String projectName;
	// 目标分支
	@TableColumn(name = "branch")
	private String branch;

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getMergeid() {
		return mergeid;
	}

	public void setMergeid(String mergeid) {
		this.mergeid = mergeid;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

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

	@Override
	public String toString() {
		return "MegerRequestPO [id=" + id + ", authorName=" + authorName
				+ ", mergeid=" + mergeid + ", requestTime=" + requestTime
				+ ", status=" + status + ", description=" + description
				+ ", title=" + title + ", version=" + version + ", isVersion="
				+ isVersion + ", relationId=" + relationId + ", projectId="
				+ projectId + ", projectName=" + projectName + ", branch="
				+ branch + "]";
	}


}
