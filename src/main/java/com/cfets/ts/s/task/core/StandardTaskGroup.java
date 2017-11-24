package com.cfets.ts.s.task.core;

import com.cfets.ts.s.task.HashUtil;
import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskResult;

public class StandardTaskGroup implements TaskGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4719129024544618895L;

	private final String name;

	public StandardTaskGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public TaskResult execute() {
		return new StandardTaskResult(true, "执行任务组[" + this.name + "]");
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public TaskType type() {
		return TaskType.ROOT;
	}

	@Override
	public TaskGroup group() {
		return this;
	}

	@Override
	public String toString() {
		return name;
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
		StandardTaskGroup other = (StandardTaskGroup) o;
		if (this.name == null || other.name == null) {
			return false;
		}
		if (this.name != other.name) {
			return false;
		}
		return true;
	}

}
