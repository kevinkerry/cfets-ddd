/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.gitlab.core.exceptions;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年3月4日
 * 
 * @history
 * 
 */
public class GitlabException extends RuntimeException {

	private static final long serialVersionUID = -7170479083640227546L;

	public GitlabException(String msg) {
		super(msg);
	}

}
