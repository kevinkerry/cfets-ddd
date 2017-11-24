package com.cfets.ts.s.task.test;

import com.cfets.ts.s.task.Task;
import com.cfets.ts.s.task.TaskGroup;
import com.cfets.ts.s.task.TaskResult;

public class InterruptedTask implements Task {

	@Override
	public TaskResult execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskType type() {
		return TaskType.BLOCKED;
	}

	@Override
	public TaskGroup group() {
		// TODO Auto-generated method stub
		return null;
	}
}
