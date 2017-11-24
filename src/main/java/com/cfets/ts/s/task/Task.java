/**
 * 辅助交易系统ts-s-task
 */
package com.cfets.ts.s.task;

/**
 * <h2>核心任务接口</h2>
 * 
 * <br>
 * <strong>注意：任务必须保证无状态</strong>
 * 
 */
public interface Task extends java.io.Serializable {

	/**
	 * 执行任务并返回任务的执行成功与失败
	 * 
	 * @return
	 */
	TaskResult execute();

	/**
	 * 指定任务名称，任务名称必须全局唯一的，以英文表达为最佳，否则启动时候会报错
	 * 
	 * @return
	 */
	String name();

	/**
	 * 定义任务类型
	 * 
	 * @return
	 */
	TaskType type();

	/**
	 * 一个任务在定义时就在一个固定的组内
	 * 
	 * @return
	 */
	TaskGroup group();

	/**
	 * 
	 * 任务类型定义
	 */
	public static enum TaskType {

		BLOCKED, STREAM, ROOT

	}

}
