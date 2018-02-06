package com.cfets.ddd.d.platform;

import com.cfets.ddd.d.core.logger.DLogger;
import com.cfets.ddd.p.core.component.GenericComponent;
import com.cfets.ddd.p.core.component.Plugin;
import org.springframework.stereotype.Component;


/**
 * Platform资源注册
 *
 * @author lijian
 * @since 0.1.1.0
 */
@Component
public class PlatformComponent extends GenericComponent {

    private static final DLogger logger = DLogger
            .getLogger(PlatformComponent.class);


    @Override
    public Plugin getPlugin() {
        return Plugin.PluginBuilder.newPlugin("ddd-p-platform");
    }

    @Override
    public void toActivate() {

    }

    @Override
    public Position toFix() {
        return Position.DOMAIN;
    }
}
