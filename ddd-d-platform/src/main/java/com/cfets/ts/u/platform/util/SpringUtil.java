package com.cfets.ts.u.platform.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Component
public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext application;
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(SpringUtil.application==null){
			SpringUtil.application = applicationContext;
		}

	}


	public static ApplicationContext getApplication() {
		return application;
	}


	public static <T> T getBean(Class<T> clazz){
		return getApplication().getBean(clazz); 
	}

}
