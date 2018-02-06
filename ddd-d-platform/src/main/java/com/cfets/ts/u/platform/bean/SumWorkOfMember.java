package com.cfets.ts.u.platform.bean;

import java.io.Serializable;
import java.util.Date;

//工作统计量事件表
public class SumWorkOfMember implements Serializable {
	private static final long serialVersionUID = 2206742402985917016L;
	private Long id;
	private Integer userId;// 用户ID
	private Integer addNum;// 增加行数
	private Integer delNum;// 减少行数
	private Date subTime;// 提交时间
	private Date subDate;// 提交日期
	private String month;// 月份
	private String year;// 年份
	private String commitId;// 提交的commitId
	private String plugin;// //提交的模块
	private String branch;// 提交的分支

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getAddNum() {
		return addNum;
	}

	public void setAddNum(Integer addNum) {
		this.addNum = addNum;
	}

	public Integer getDelNum() {
		return delNum;
	}

	public void setDelNum(Integer delNum) {
		this.delNum = delNum;
	}

	public Date getSubTime() {
		return subTime;
	}

	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}

	public Date getSubDate() {
		return subDate;
	}

	public void setSubDate(Date subDate) {
		this.subDate = subDate;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
