/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.gitlab.core.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;

import javax.net.ssl.SSLHandshakeException;

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
public class GitlabExceptionHandler {

	public static GitlabException handle(Throwable e) {
		if (e instanceof SSLHandshakeException) {
			return new GitlabException("Invalid TLS Certificate: "
					+ e.getMessage());
		} else if (e instanceof ConnectException) {
			return new GitlabException("Connection refused");
		} else if (e instanceof NoRouteToHostException) {
			return new GitlabException("No route to host");
		} else if (e instanceof FileNotFoundException) {
			return new GitlabException("Invalid path in host");
		} else if (e instanceof IOException) {
			return new GitlabException(
					"Invalid username/password/private token combination");
		}
		return new GitlabException("Unknown Exception: " + e.getMessage());
	}

}
