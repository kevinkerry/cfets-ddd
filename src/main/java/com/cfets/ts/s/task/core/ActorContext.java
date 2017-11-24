package com.cfets.ts.s.task.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cfets.ts.s.task.DefaultTaskIdGenerator;
import com.cfets.ts.s.task.TaskElement;
import com.cfets.ts.s.task.TaskIdGenerator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * 任务执行引擎上下文容器
 * 
 * @author pluto
 *
 */
public class ActorContext implements  TaskContext {

	private static final Logger logger = LoggerFactory.getLogger("[TASK ENGINE]");

	private TaskIdGenerator idGenerator;

	private TaskManager manager;

	private boolean ispermitted;

	private ActorSystem actorSystem;

	private Map<String, ActorRef> map = new HashMap<String, ActorRef>();

	private ActorContext() {
		this.idGenerator = new DefaultTaskIdGenerator();
		this.manager = new TaskManager();
		this.ispermitted = false;
	}

	public void init() {
		this.initActorSystem();
		Set<TaskProxy> set = manager.getAllGroupProxy();
		set.forEach(v -> {
			TaskElement te = manager.getTaskTopology(v.group());
			map.put(v.getTaskId(), this.createTeamLeader(v, te));
		});
	}

	/**
	 * <li>管理调度服务</li>
	 * <li>配置相关参数</li>
	 * <li>日志功能</li>
	 */
	private void initActorSystem() {
		actorSystem = ActorSystem.create("core-system");
	}

	private ActorRef createTeamLeader(TaskProxy group, TaskElement te) {
		return actorSystem.actorOf(Props.create(TeamLeader.class, group.getTaskId(), te));
	}

	public void invokeTeamLeader(String taskId, TaskRequestEvent event) {
		map.get(taskId).tell(event, ActorRef.noSender());
	}

	public boolean isPermitted() {
		return this.ispermitted;
	}

	public void forbidRegistration() {
		this.ispermitted = false;
		logger.info("Spring容器已经初始化完毕，禁止继续注册Task");
	}

	public TaskIdGenerator getTaskIdGenerator() {
		return this.idGenerator;
	}

	public TaskManager getTaskManager() {
		return this.manager;
	}

	private static class TaskContextHolder {
		private static final ActorContext instance = new ActorContext();
	}

	public static final ActorContext get() {
		return TaskContextHolder.instance;
	}

	//@Override
	public void destroy() throws Exception {
		logger.info("任务执行引擎上下文已销毁");

	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public TaskContext getTaskContext() {
		return this;
	}

}
