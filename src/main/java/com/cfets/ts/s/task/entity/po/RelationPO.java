package com.cfets.ts.s.task.entity.po;

import java.io.Serializable;

public class RelationPO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1601176426787591508L;
	private Long relationId;
	private Long scheduleId;
	private Long taskId;
	private Integer taskStep;
	
	public Long getRelationId() {
		return relationId;
	}
	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Integer getTaskStep() {
		return taskStep;
	}
	public void setTaskStep(Integer taskStep) {
		this.taskStep = taskStep;
	}
	public RelationPO() {
		super();
	}
	public RelationPO(Long relationId, Long scheduleId, Long taskId,
			Integer taskStep) {
		super();
		this.relationId = relationId;
		this.scheduleId = scheduleId;
		this.taskId = taskId;
		this.taskStep = taskStep;
	}
	
	
}
