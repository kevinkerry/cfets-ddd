package com.cfets.ts.s.task;

import org.junit.Test;

import com.cfets.ts.s.task.core.Leader;
import com.cfets.ts.s.task.core.TaskRequestEvent;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

public class LeaderTest {

	ActorSystem system = ActorSystem.create("test-task");

	@Test
	public void createLeader() {
		TestActorRef<Leader> actorRef = TestActorRef.create(system, Props.create(Leader.class,"ga","l-1"));
		TaskRequestEvent event = new TaskRequestEvent("测试启动Leader", "Leader成功执行任务");
		actorRef.tell(event, ActorRef.noSender());

	}

}
