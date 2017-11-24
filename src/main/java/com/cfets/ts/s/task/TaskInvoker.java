/**
 * 辅助交易系统ts-s-task
 */
package com.cfets.ts.s.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cfets.ts.s.task.entity.po.Progress;
import com.cfets.ts.s.task.entity.po.TaskPO;
import com.cfets.ts.s.task.service.TaskService;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * 
 * @author pluto
 * @create on 2017年2月6日
 * 
 * @history
 * 
 */
public class TaskInvoker {
	
	private TaskService service = new TaskService();

	private Progress progress = new Progress();

	private TaskInvoker() {

	}

	private static class TaskInvokerHolder {
		static TaskInvoker instance = new TaskInvoker();
	}

	/**
	 * 获取任务执行信息
	 */
	public Progress getTaskProgress() {
		return this.progress;
	}

	/**
	 * 单例模式获取实例
	 * 
	 * @return
	 * @Company TCS
	 * @author zhoujun
	 * @date 2016年12月26日 上午10:07:47
	 */
	public static TaskInvoker get() {
		return TaskInvokerHolder.instance;
	}

	/**
	 * 启动某任务调度下所有任务
	 * 
	 * @description:[invokeAllTask]
	 * @param scheduleId
	 *            任务调度ID
	 * @return void
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws @author
	 *             pluto 2017年2月6日
	 */
	public void invokeAllTask(long scheduleId)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		List<TaskPO> taskPOs = service.getTaskListByScheduleId(scheduleId);
		progress.setTaskNum(taskPOs.size());
		List<Map<String, String>> list = progress.getTaskInfo();

		for (int i = 0; i < taskPOs.size(); i++) {
			TaskPO item = taskPOs.get(i);
			String taskName = item.getTaskName();
			Map<String, String> map = new HashMap<String, String>();
			map.put("info", taskName + "....开始执行");
			map.put("percent", progress.getPercent());
			list.add(map);
			Class<?> clazz = Class.forName(item.getClassName());
			Task task = (Task) clazz.newInstance();
			TaskResult flag = task.execute();
			progress.setCompleteNum(i + 1);
			if (flag.isFinished()) {
				map = new HashMap<String, String>();
				map.put("info", taskName + "....执行成功");
				map.put("percent", progress.getPercent());
				list.add(map);
			} else {
				map = new HashMap<String, String>();
				map.put("info", taskName + "....执行失败");
				map.put("percent", progress.getPercent());
				list.add(map);
			}
		}
	}

}
