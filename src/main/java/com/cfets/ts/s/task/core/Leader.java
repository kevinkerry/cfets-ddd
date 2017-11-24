package com.cfets.ts.s.task.core;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Leader extends AbstractActor {

	private final LoggingAdapter LOG = Logging.getLogger(getContext().getSystem(), this);

	final String leaderId;

	final String groupId;

	public Leader(String groupId, String leaderId) {
		this.groupId = groupId;
		this.leaderId = leaderId;
	}

	public static Props props() {
		return Props.create(Leader.class);
	}

	@Override
	public void preStart() {
		LOG.info("Leader启动工作");
	}

	@Override
	public void postStop() {
		LOG.info("Leader停止工作");
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(TaskRequestEvent.class, r -> {
			LOG.info(this.leaderId + "接收任务-->" + r.getRequestObject().toString());
		}).build();

	}

}
