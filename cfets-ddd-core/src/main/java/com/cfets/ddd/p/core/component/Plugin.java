package com.cfets.ddd.p.core.component;

import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;

/**
 * Created by pluto on 22/01/2018.
 */
public class Plugin implements Project {

    private String name;

    protected Plugin(String name){
        this.name=name;
    }

    public String name(){
        return this.name;
    }

    public static class PluginBuilder {

        public static Plugin newPlugin(String pluginName){
            Plugin p=new Plugin(pluginName);
            return p;
        }

    }
}
