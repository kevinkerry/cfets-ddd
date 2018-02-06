package com.cfets.ddd.p.core.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by pluto on 22/01/2018.
 */
public abstract class GenericComponent implements Component,ApplicationListener<ContextRefreshedEvent>{


    private static final Logger logger= LoggerFactory.getLogger(GenericComponent.class);


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(contextRefreshedEvent.getApplicationContext().getParent()==null){
            toActivate();
            logger.info("The component of {} has already activated",this.getPlugin());
        }

    }



}
