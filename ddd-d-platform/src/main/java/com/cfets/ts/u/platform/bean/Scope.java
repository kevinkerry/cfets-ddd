/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.u.platform.bean;

import org.apache.commons.lang.StringUtils;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年2月27日
 * 
 * @history
 * 
 */
public enum Scope {

	TEST("test"), PROVIDED("provided"), COMPILE("compile"), SYSTEM("system"), EXTEND(
			"extend"), BELONG("belong");

	public static final String SCOPE_TEST = "test";

	public static final String SCOPE_PROVIDED = "provided";

	public static final String SCOPE_COMPILE = "compile";

	public static final String SCOPE_SYSTEM = "system";

	public static final String SCOPE_EXTEND = "extend";

	public static final String SCOPE_BELONG = "belong";

	private String name;

	Scope(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public static Scope obtain(String content) {
		if (StringUtils.isNotBlank(content)) {
			switch (content) {
			case SCOPE_TEST:
				return TEST;
			case SCOPE_PROVIDED:
				return PROVIDED;
			case SCOPE_COMPILE:
				return COMPILE;
			case SCOPE_SYSTEM:
				return SYSTEM;
			case SCOPE_EXTEND:
				return EXTEND;
			case SCOPE_BELONG:
				return BELONG;
			}
		}
		return COMPILE;
	}


}
