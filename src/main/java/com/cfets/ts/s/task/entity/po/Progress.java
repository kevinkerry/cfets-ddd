package com.cfets.ts.s.task.entity.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Progress implements Serializable{
	
	private static final long serialVersionUID = 819641582397600019L;
	
	private int taskNum; //总任务数量
	private int completeNum; //任务已完成数量
	private String percent; //任务进程百分比
	private List<Map<String,String>> taskInfo = new ArrayList<Map<String,String>>(); // 任务执行信息
	
	public int getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	public int getCompleteNum() {
		return completeNum;
	}
	public void setCompleteNum(int completeNum) {
		this.completeNum = completeNum;
	}
	public String getPercent() {
		if(0!=taskNum){
			int val=(int) (((float)completeNum/taskNum)*100);
			percent = val+"%";
		}else{
			percent = "0%";
		}
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public List<Map<String, String>> getTaskInfo() {
		return taskInfo;
	}
	public void setTaskInfo(List<Map<String, String>> taskInfo) {
		this.taskInfo = taskInfo;
	}
	
	
}
