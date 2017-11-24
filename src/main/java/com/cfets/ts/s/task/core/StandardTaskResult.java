package com.cfets.ts.s.task.core;

import com.cfets.ts.s.task.TaskResult;

public class StandardTaskResult implements TaskResult {

	private boolean isFinished;

	private Object taskResult;

	public StandardTaskResult(boolean isFinished, Object taskResult) {
		this.isFinished = isFinished;
		this.taskResult = taskResult;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public Object getTaskResult() {
		return taskResult;
	}

}
