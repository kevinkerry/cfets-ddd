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
public class UnknownProjectException extends GitlabException {

	/**
	  *  
	  */
	private static final long serialVersionUID = 9197943019104391768L;

	public UnknownProjectException(String project) {
		super("Unknown project " + project + " or insufficient access rights");
	}

}
