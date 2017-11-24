package com.cfets.ts.s.task;

import com.cfets.ts.s.task.core.Worker;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class WorkerTest {

	public static void main(String[] args) throws java.io.IOException {
		
		ActorSystem system = ActorSystem.create("testSystem");

		ActorRef firstRef = system.actorOf(Props.create(Worker.class), "first-actor");
		System.out.println("First: " + firstRef);
		firstRef.tell("printit", ActorRef.noSender());

		System.out.println(">>> Press ENTER to exit <<<");
		try {
			System.in.read();
		} finally {
			system.terminate();
		}
	}

}
