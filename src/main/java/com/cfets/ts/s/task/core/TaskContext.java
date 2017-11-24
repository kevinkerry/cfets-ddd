package com.cfets.ts.s.task.core;

import org.slf4j.Logger;

public interface TaskContext {

	Logger getLogger();

	TaskContext getTaskContext();

}
