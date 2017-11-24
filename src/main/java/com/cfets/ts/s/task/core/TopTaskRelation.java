package com.cfets.ts.s.task.core;

import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskRelation;

/**
 * 任务关系
 * 
 * @author pluto
 *
 */
public class TopTaskRelation extends AbstractTaskRelation implements TaskRelation{

	public TopTaskRelation(TaskProxy child, TaskGroup group) {
		super(new TaskProxy(group), child, group);
	}

}
