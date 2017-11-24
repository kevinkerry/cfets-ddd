package com.cfets.ts.s.task.exception;

public class TaskInitializationException extends RuntimeException {

	private static final long serialVersionUID = 1636028785558340461L;

	public TaskInitializationException() {
		super("ts-s-task的任务初始化异常");
	}

	public TaskInitializationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TaskInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskInitializationException(String message) {
		super(message);
	}

	public TaskInitializationException(Throwable cause) {
		super(cause);
	}

}
