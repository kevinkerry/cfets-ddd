package com.cfets.ts.s.task.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cfets.ts.s.task.HashUtil;
import com.cfets.ts.s.task.Task;
import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskResult;
import com.cfets.ts.s.task.core.StandardTaskResult;

public class SimpleTask implements Task {

	private static final long serialVersionUID = -2578086884306186481L;

	private static final Logger logger = LoggerFactory.getLogger(SimpleTask.class);

	private String name;

	private String info;

	private final TaskGroup group;

	public SimpleTask(TaskGroup group, String name, String info) {
		this.name = name;
		this.info = info;
		this.group = group;
	}

	@Override
	public TaskResult execute() {
		logger.info("处理简单任务" + name);
		return new StandardTaskResult(true, info);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public TaskType type() {
		return TaskType.BLOCKED;
	}

	@Override
	public TaskGroup group() {
		return this.group;
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
		SimpleTask other = (SimpleTask) o;
		if (this.name == null || other.name == null || this.group == null || other.group == null) {
			return false;
		}
		if (this.name != other.name || !this.group.equals(other.group)) {
			return false;
		}
		return true;
	}

}
