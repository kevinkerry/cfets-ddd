package com.cfets.ddd.d.core.spring;

import com.cfets.ddd.d.core.logger.DLogger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextVisitor implements ApplicationContextAware {

	private static final DLogger logger=DLogger.getLogger(SpringContextVisitor.class);


	private static ApplicationContext application;
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(SpringContextVisitor.application==null){
			SpringContextVisitor.application = applicationContext;

		}

	}


	public static ApplicationContext getApplication() {
		return application;
	}


	public static <T> T getBean(Class<T> clazz){
		return getApplication().getBean(clazz); 
	}

}
