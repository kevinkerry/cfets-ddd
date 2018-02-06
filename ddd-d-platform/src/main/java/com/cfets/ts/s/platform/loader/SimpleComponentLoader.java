/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.loader;

import com.cfets.ts.s.platform.ComponentLoader;
import com.cfets.ts.s.platform.ComponentManager;
import com.cfets.ddd.d.platform.repository.domain.Component;
import com.cfets.ddd.d.platform.repository.domain.ComponentGroup;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年2月27日
 * 
 * @history
 * 
 */
public class SimpleComponentLoader implements ComponentLoader {

	@Override
	public ComponentGroup load(ComponentGroup componentGroup) {
		Component sI18n = new Component();
		sI18n.setGroupId("com.cfets.ts");
		sI18n.setArtifactId("ts-s-i18n");
		sI18n.setVersion("0.1.8.0");
		ComponentManager.get().registerComponent(sI18n);
		Component sLog = new Component();
		sLog.setGroupId("com.cfets.ts");
		sLog.setArtifactId("ts-s-log");
		sLog.setVersion("0.1.7.0");
		ComponentManager.get().registerComponent(sLog);
		Component uException = new Component();
		uException.setGroupId("com.cfets.ts");
		uException.setArtifactId("ts-u-exception");
		uException.setVersion("0.1.10.0");
		ComponentManager.get().registerComponent(uException);
		Component sDict = new Component();
		sDict.setGroupId("com.cfets.ts");
		sDict.setArtifactId("ts-s-dict");
		sDict.setVersion("0.1.7.0");
		return componentGroup;
	}

}
