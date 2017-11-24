package com.cfets.ts.s.task;

import com.cfets.ts.s.task.core.TaskProxy;

/**
 * 
 * 任务关系
 * 
 * @author pluto
 *
 */
public interface TaskRelation {

	TaskGroup getTaskGroup();

	int getPosition();

	TaskProxy getParent();

	TaskProxy getChild();

}
