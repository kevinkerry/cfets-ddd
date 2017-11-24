package com.cfets.ts.s.task.core;

import java.util.List;

import com.cfets.ts.s.task.TaskElement;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * 一个工人一次只能完成一项任务
 * 
 * @author pluto
 *
 */
public class Worker extends AbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public static Props props() {
		return Props.create(Worker.class);
	}

	private TaskElement element;

	private TaskProxy proxy;

	public Worker(String groupId, TaskElement element) {
		this.proxy = element.getCurrentRelation().getChild();
		this.element = element;
	}

	@Override
	public void preStart() {
		log.info("Worker启动工作");
	}

	@Override
	public void postStop() {
		log.info("Worker停止工作");
	}

	private ActorRef nextWorker(TaskElement element) {
		TaskProxy proxy = element.getCurrentRelation().getChild();
		return context().actorOf(Props.create(Worker.class, proxy.getTaskId(), element));
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(TaskRequestEvent.class, r -> {
			this.proxy.execute();
			List<TaskElement> es = element.getChildren();
			if (es != null && es.size() > 0) {
				for (TaskElement te : es) {
					ActorRef workerRef = nextWorker(te);
					workerRef.tell(r, sender());
				}
			}

		}).build();
	}

}
