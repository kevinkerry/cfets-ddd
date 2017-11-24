package com.cfets.ts.s.task.core;

/**
 * 不可变事件
 * 
 * @author pluto
 *
 */
public class TaskRequestEvent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1236873054070247140L;

	private final String task;

	private final Object requestObject;

	public TaskRequestEvent(String task, Object requestObject) {
		this.task = task;
		this.requestObject = requestObject;
	}

	public String getTask() {
		return task;
	}

	public Object getRequestObject() {
		return requestObject;
	}

}
