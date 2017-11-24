package com.cfets.ts.s.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cfets.ts.s.task.core.TaskProxy;

/**
 * 任务编排拓扑结构：任务必须是一个有向无环图
 * 
 * @author pluto
 *
 */
public class TaskElement {

	private TaskProxy currentTask;

	private TaskRelation currentRelation;

	private List<TaskElement> parents;// 对于根节点来,head等于自己

	private List<TaskElement> children;

	public TaskElement(TaskRelation relation) {
		this.currentRelation = relation;
		this.currentTask = relation.getParent();
		this.children = new ArrayList<TaskElement>();
		this.parents = new ArrayList<TaskElement>();
	}

	public TaskProxy getCurrentTask() {
		return this.currentTask;
	}

	public TaskRelation getCurrentRelation() {
		return this.currentRelation;
	}

	public void addChild(TaskElement element) {
		this.children.add(element);
	}

	public List<TaskElement> getChildren() {
		return this.children;
	}

	public void addParent(TaskElement element) {
		this.parents.add(element);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TaskElement[relation=");
		sb.append(currentRelation);
//		if (parents.size() > 0) {
//			sb.append(",parents=");
//			sb.append(Arrays.toString(parents.toArray()));
//		}
		if (children.size() > 0) {
			sb.append(",children=");
			sb.append(Arrays.toString(children.toArray()));
		}
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
		TaskElement other = (TaskElement) o;
		if (this.currentTask == null || other.currentTask == null || this.currentRelation == null
				|| other.currentRelation == null) {
			return false;
		}
		if (this.currentTask != other.currentTask || this.currentRelation != other.currentRelation) {
			return false;
		}
		return true;
	}

}
