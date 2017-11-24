package com.cfets.ts.s.task.akka;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {

	private Object target;

	public ProxyHandler(Object target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object o = method.invoke(target, args);
		return o;
	}

}
