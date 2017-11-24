package com.cfets.ts.s.task.entity.po;

import java.io.Serializable;

public class TaskPO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1909411227307833667L;
	private Long taskId;	
	private Long scheduleId;
	private String className;
	private String taskName;
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public TaskPO() {
		super();
	}
	public TaskPO(Long taskId, Long scheduleId, String className,
			String taskName) {
		super();
		this.taskId = taskId;
		this.scheduleId = scheduleId;
		this.className = className;
		this.taskName = taskName;
	}
	
}
