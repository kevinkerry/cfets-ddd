package com.cfets.ts.s.task.core;

import com.cfets.ts.s.task.TaskElement;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * 一个TeamLeader负责处理一组任务，负责创建和管理子任务
 * 
 * @author pluto
 *
 */
public class TeamLeader extends Leader {

	private final LoggingAdapter LOG = Logging.getLogger(getContext().getSystem(), this);

	private TaskElement element;

	public TeamLeader(String groupId, TaskElement element) {
		super(groupId, groupId);
		this.element = element;
	}

	private ActorRef employWorker(TaskElement element) {
		TaskProxy proxy = element.getCurrentRelation().getChild();
		return context().actorOf(Props.create(Worker.class, proxy.getTaskId(), element));
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(TaskRequestEvent.class, r -> {
			LOG.info(this.leaderId + "接收任务-->" + r.getRequestObject().toString());
			ActorRef workerRef = employWorker(this.element);
			workerRef.tell(r, self());
		}).build();

	}

}
