package com.cfets.ddd.p.core.component;

/**
 * Created by pluto on 22/01/2018.
 */
public interface Component {

    Plugin getPlugin();

    void toActivate();

    Position toFix();

    public static enum Position {

        DOMAIN,PLUGIN

    }

}
