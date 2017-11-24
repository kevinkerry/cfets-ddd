package com.cfets.ts.s.task.core;

import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskRelation;

/**
 * 任务关系
 * 
 * @author pluto
 *
 */
public class StandardTaskRelation extends AbstractTaskRelation implements TaskRelation {

	public StandardTaskRelation(TaskProxy parent, TaskProxy child, TaskGroup group) {
		super(parent, child, group);
	}

	

}
