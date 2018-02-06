/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.fetcher;

import com.cfets.ts.s.platform.gitlab.core.ConnectionManager;
import com.cfets.ts.s.platform.gitlab.core.Repositories;
import com.cfets.ts.s.platform.gitlab.core.TaskRepository;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年3月4日
 * 
 * @history
 * 
 */
public class GitlabFetcher {

	public Repositories fetch() {
		String username = "lijian";
		String privateToken = "Wy_3Rc4fT472rXCmsMxx";
		String url = "http://gitlab.scm.cfets.com/TS/ts-u-crv.git";// 引子工程
		TaskRepository repository = new TaskRepository("TS");
		repository.useToken();
		repository.setAuthenticationCredentials(url, username, privateToken);
		ConnectionManager.get(repository);
		return ConnectionManager.getRepos();
	}

}
