package com.cfets.ts.u.platform.web;

import com.cfets.ts.u.platform.api.commond.MergeCommond;
import com.cfets.ts.u.platform.service.PlatformService;

public class PlatFormMessageOne implements Runnable {
	
	private MergeCommond commmond;
	private PlatformService platformService;

	public PlatFormMessageOne(MergeCommond commmond,PlatformService platformService) {
		this.commmond = commmond;
		this.platformService=platformService;
	}

	@Override
	public void run() {
		this.platformService.dealMergeAccept(commmond);
	}

}
