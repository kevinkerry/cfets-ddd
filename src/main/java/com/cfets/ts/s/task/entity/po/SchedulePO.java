package com.cfets.ts.s.task.entity.po;

import java.io.Serializable;

public class SchedulePO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3433794150699474722L;
	private Long scheduleId;
	private String  scheduleName;
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public SchedulePO() {
		super();
	}
	public SchedulePO(Long scheduleId, String scheduleName) {
		super();
		this.scheduleId = scheduleId;
		this.scheduleName = scheduleName;
	}
	
}
