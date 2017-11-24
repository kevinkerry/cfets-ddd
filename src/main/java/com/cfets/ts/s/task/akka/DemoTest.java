package com.cfets.ts.s.task.akka;

public class DemoTest {
	public static void main(String[] args) throws Exception {
		ITask target=new TaskImpl();
		ITask proxy=(ITask) ProxyFactory.getProxyInstance(target);
		proxy.execute();		
	}
}
