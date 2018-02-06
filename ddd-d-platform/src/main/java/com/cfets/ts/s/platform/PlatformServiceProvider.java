package com.cfets.ts.s.platform;

import java.util.List;

import com.cfets.cwap.s.spi.Provider;
import com.cfets.ts.s.platform.bean.ComponentPO;
import com.cfets.ts.s.platform.service.ComponentService;
import com.cfets.ts.s.platform.service.impl.ComponentServiceImpl;


public class PlatformServiceProvider implements Provider{
	private ComponentService componetService = new ComponentServiceImpl();
	public List<ComponentPO> findAll() {
		return componetService.findall();
	}
	public static PlatformServiceProvider get() {
		// TODO Auto-generated method stub
		return InnerPlatform.intance;
	}
	
	
	private static class InnerPlatform {
		public static PlatformServiceProvider intance = new PlatformServiceProvider();
	}
	
}
