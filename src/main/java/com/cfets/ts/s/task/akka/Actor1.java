package com.cfets.ts.s.task.akka;



import java.util.concurrent.TimeUnit;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class Actor1 extends UntypedActor {
	
	
	@Override
	public void preStart() throws Exception {
		final ActorRef childActor=getContext().actorOf(Props.create(Actor2.class), "child-actor");
		
		final ActorRef childActor2=getContext().actorOf(Props.create(Actor3.class), "child-actor2");
	
		Timeout timeout=new Timeout(Duration.create(3, TimeUnit.SECONDS));
		Future<Object> future1=Patterns.ask(childActor, Actor2.Msg.HANDLE, timeout);
		Future<Object> future2=Patterns.ask(childActor2, Actor3.Msg.HANDLE, timeout);
		Actor2.Msg result1=(Actor2.Msg) Await.result(future1, timeout.duration());
		Actor3.Msg result2= (Actor3.Msg) Await.result(future2, timeout.duration());
		System.out.println("result1="+result1);
		System.out.println("result2="+result2);
		
		
		if(result1==Actor2.Msg.DONE&&result2==Actor3.Msg.DONE){
			final ActorRef childActor3=getContext().actorOf(Props.create(Actor4.class), "child-actor3");
			childActor3.tell(Actor4.Msg.HANDLE, getSelf());
		}else{
			System.out.println("error");
		}
		
		
		
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		System.out.println(this.getClass().getSimpleName()+" Received Event:"+msg);
		if(msg==Actor2.Msg.DONE){
			System.out.println("actor2 done");
		}else if (msg==Actor3.Msg.DONE) {
			System.out.println("actor3 done");
		}else if (msg==Actor4.Msg.DONE) {
			System.out.println("actor4 done");
		}
		else{
			unhandled(msg);
		}
	}

}
