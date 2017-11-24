package com.cfets.ts.s.task.core;

import com.cfets.ts.s.task.HashUtil;
import com.cfets.ts.s.task.Task;
import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskRelation;
import com.cfets.ts.s.task.exception.TaskInitializationException;

/**
 * 抽象的任务关系
 * 
 * @author pluto
 *
 */
public abstract class AbstractTaskRelation implements TaskRelation {

	final TaskGroup group;

	final TaskProxy parent;

	final TaskProxy child;

	int position;

	public AbstractTaskRelation(TaskProxy parent, TaskProxy child, TaskGroup group) {
		this.parent = parent;
		this.child = child;
		this.group = group;
	}

	public boolean isParent(Task task) {
		if (task.equals(parent)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public TaskGroup getTaskGroup() {
		return group;
	}

	//@Override
	public void afterPropertiesSet() throws Exception {
		if (group == null) {
			throw new TaskInitializationException("创建StandardTaskRelation时必须指定TaskGroup");
		}
		if (parent == null) {
			throw new TaskInitializationException("创建StandardTaskRelation时必须指定父Task");
		}
		if (child == null) {
			throw new TaskInitializationException("创建StandardTaskRelation时必须指定子Task");
		}

	}

	protected void setPosition(int position) {
		this.position = position;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public TaskProxy getParent() {
		return this.parent;
	}

	@Override
	public TaskProxy getChild() {
		return this.child;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName());
		sb.append("[group=");
		sb.append(group.name());
		sb.append(",parent=");
		sb.append(parent.name());
		sb.append(",child=");
		sb.append(child.name());
		sb.append("]");
		return sb.toString();
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
		AbstractTaskRelation other = (AbstractTaskRelation) o;
		if (this.parent == null || other.parent == null || this.group == null || other.group == null
				|| this.child == null || other.parent == null) {
			return false;
		}
		if (this.group != other.group || this.parent != other.parent || this.child != other.child) {
			return false;
		}
		return true;
	}

}
