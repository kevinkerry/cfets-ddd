package com.cfets.ts.s.task.core;

import com.cfets.ts.s.task.HashUtil;
import com.cfets.ts.s.task.Task;
import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskResult;

/**
 * 任务执行的唯一任务代理者
 * 
 * @author pluto
 *
 */
public class TaskProxy implements Task {

	private static final long serialVersionUID = 8217372011326083289L;

	private Task delegate;

	private String taskId;

	public TaskProxy(Task task) {
		this.delegate = task;
		this.taskId = task.name() + "[" + ActorContext.get().getTaskIdGenerator().createId() + "]";
	}

	public String getTaskId() {
		return this.taskId;
	}

	@Override
	public TaskResult execute() {
		return delegate.execute();
	}

	@Override
	public String name() {
		return delegate.name();
	}

	@Override
	public TaskType type() {
		return delegate.type();
	}

	@Override
	public TaskGroup group() {
		return delegate.group();
	}

	@Override
	public String toString() {
		return taskId;
	}

	public Task getDelegate() {
		return this.delegate;
	}

	@Override
	public int hashCode() {
		return HashUtil.lowerHashCode(this.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o.getClass() == this.getClass())) {
			return false;
		}
		TaskProxy other = (TaskProxy) o;
		if (this.taskId == null || other.taskId == null) {
			return false;
		}
		if (this.taskId != other.taskId) {
			return false;
		}
		return true;
	}

}
