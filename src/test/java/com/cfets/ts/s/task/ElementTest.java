package com.cfets.ts.s.task;

import org.junit.Test;

import com.cfets.ts.s.task.core.StandardTaskGroup;
import com.cfets.ts.s.task.test.SimpleTask;

public class ElementTest {

	private void testTaskList() {
		TaskGroup g1 = new StandardTaskGroup("测试计划1");
		Task a = new SimpleTask(g1, "A", "执行A");
		Task b = new SimpleTask(g1, "B", "执行B");
		Task c = new SimpleTask(g1, "C", "执行C");
		Task d = new SimpleTask(g1, "D", "执行D");
		Task e = new SimpleTask(g1, "E", "执行E");
		// 核心API，建立任务拓扑
//		TaskProvider.get().registerTaskList(g1, a, b, c, d, e).addRelation(g1, a, b).addRelation(g1, a, c)
//				.addRelation(g1, b, d).addRelation(g1, c, d).addRelation(g1, c, e).build();

	}

	public static void main(String[] args) {
		new ElementTest().testTaskList();
	}

	@Test
	public void start() {
		testTaskList();
	}

}
