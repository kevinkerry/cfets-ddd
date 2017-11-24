package com.cfets.ts.s.task.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cfets.ts.s.task.Task;
import com.cfets.ts.s.task.TaskElement;
import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskRelation;
import com.cfets.ts.s.task.exception.TaskInitializationException;

public class TaskManager {

	private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

	private Map<TaskGroup, TaskProxy> groups = new HashMap<TaskGroup, TaskProxy>();

	private Map<Task, TaskProxy> delegater = new HashMap<Task, TaskProxy>();

	private Map<TaskGroup, Map<String, TaskProxy>> tasks = new HashMap<TaskGroup, Map<String, TaskProxy>>();

	private Map<TaskGroup, CopyOnWriteArraySet<TaskRelation>> relations = new HashMap<TaskGroup, CopyOnWriteArraySet<TaskRelation>>();

	private Map<TaskGroup, TaskElement> elements = new HashMap<TaskGroup, TaskElement>();

	public void registerTask(TaskProxy proxy) {
		if (tasks.containsKey(proxy.group())) {
			Map<String, TaskProxy> temp = tasks.get(proxy.group());
			if (temp.containsKey(proxy.name())) {
				throw new TaskInitializationException("同一个TaskGroup内不能重复注册同一任务名称的任务或者请检查当前生命的任务是否和其它工程存在冲突,group:"
						+ proxy.group().name() + " name:" + proxy.name() + " task:" + proxy.getClass().getName());
			}
			temp.put(proxy.name(), proxy);
			tasks.put(proxy.group(), temp);
			delegater.put(proxy.getDelegate(), proxy);
		} else {
			Map<String, TaskProxy> map = new HashMap<String, TaskProxy>();
			map.put(proxy.name(), proxy);
			tasks.put(proxy.group(), map);
			delegater.put(proxy.getDelegate(), proxy);
			groups.put(proxy.group(), new TaskProxy(proxy.group()));
		}
	}

	public TaskProxy getTaskProxy(Task delegate) {
		return this.delegater.get(delegate);
	}

	public Set<TaskGroup> getAllGroup() {
		Set<TaskGroup> set = new HashSet<TaskGroup>();
		groups.forEach((k, v) -> {
			set.add(v.group());
		});
		return set;
	}

	public Set<TaskProxy> getAllGroupProxy() {
		Set<TaskProxy> set = new HashSet<TaskProxy>();
		groups.forEach((k, v) -> {
			set.add(v);
		});
		return set;
	}

	public TaskProxy getGroupProxy(TaskGroup group) {
		return this.groups.get(group);
	}

	public TaskProxy getGroup(String taskId) {
		return this.groups.get(taskId);
	}

	public boolean containTaskTopology(TaskGroup group) {
		return relations.containsKey(group);
	}

	public CopyOnWriteArraySet<TaskRelation> getTaskRelations(TaskGroup group) {
		return relations.get(group);
	}

	public void addTaskElement(TaskGroup group, TaskElement topology) {
		this.elements.put(group, topology);
	}

	public TaskElement getTaskTopology(TaskGroup group) {
		return elements.get(group);
	}

	public void registerTaskRelation(TaskRelation relation, TaskGroup group) {
		CopyOnWriteArraySet<TaskRelation> set = null;
		if (relations.containsKey(group)) {
			set = relations.get(group);
		} else {
			set = new CopyOnWriteArraySet<TaskRelation>();
		}
		set.add(relation);
		relations.putIfAbsent(group, set);
	}

	public TopTaskRelation getTopTaskRelation(TaskGroup group) {
		CopyOnWriteArraySet<TaskRelation> set = relations.get(group);
		Iterator<TaskRelation> it = set.iterator();
		while (it.hasNext()) {
			TaskRelation tr = it.next();
			if (tr instanceof TopTaskRelation) {
				return (TopTaskRelation) tr;
			}
		}
		return null;
	}

	public void removeTopTaskRelation(TaskProxy task, TaskGroup group) {
		CopyOnWriteArraySet<TaskRelation> set = relations.get(group);
		Iterator<TaskRelation> it = set.iterator();
		while (it.hasNext()) {
			TaskRelation tr = it.next();
			TaskProxy child = tr.getChild();
			if (tr instanceof TopTaskRelation && child.equals(task)) {
				set.remove(tr);
				relations.put(group, set);
			}
		}
		logger.debug("-->" + Arrays.toString(relations.get(group).toArray()));
	}

}
