package com.cfets.ts.s.task.akka;




import akka.actor.UntypedActor;

public class Actor3 extends UntypedActor {

	public static enum Msg{
		HANDLE,DONE;
	}
	@Override
	public void onReceive(Object msg) throws Throwable {
		System.out.println(this.getClass().getSimpleName()+" Received Event:"+msg);
		if(msg==Msg.HANDLE){
			System.out.println(this.getClass().getSimpleName()+" handling");
			getSender().tell(Msg.DONE, getSelf());
		}else{
			unhandled(msg);
		}
	}

	
}
