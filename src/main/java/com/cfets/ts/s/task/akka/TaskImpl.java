package com.cfets.ts.s.task.akka;



import akka.actor.ActorSystem;
import akka.actor.Props;

public class TaskImpl implements ITask {

	@Override
	public void execute() throws Exception {
		ActorSystem system=ActorSystem.create("handle");
		system.actorOf(Props.create(Actor1.class), "handle");
	}

}
