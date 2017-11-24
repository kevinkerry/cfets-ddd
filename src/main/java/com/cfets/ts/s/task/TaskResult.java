package com.cfets.ts.s.task;

/**
 * 任务执行结果
 * 
 * @author pluto
 *
 */
public interface TaskResult {

	/**
	 * 任务是否完成
	 * 
	 * @return
	 */
	boolean isFinished();

	/**
	 * 任务是否完成
	 * 
	 * @return
	 */
	Object getTaskResult();

}
