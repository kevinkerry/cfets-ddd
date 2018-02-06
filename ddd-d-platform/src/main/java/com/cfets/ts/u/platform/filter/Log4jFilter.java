package com.cfets.ts.u.platform.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class Log4jFilter implements Filter {
	private static final Logger logger = Logger
			.getLogger(Log4jFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	
		logger.info("filter init ------");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		MDC.put("trace", UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		logger.info("生成的::"+UUID.randomUUID());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		logger.info("filter destory ------");
		
	}

}
