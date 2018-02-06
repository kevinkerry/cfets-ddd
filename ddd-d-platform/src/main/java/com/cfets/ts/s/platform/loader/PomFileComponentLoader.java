/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.loader;

import java.io.File;
import java.util.Map;

import com.cfets.ts.s.platform.ComponentLoader;
import com.cfets.ts.s.platform.ComponentManager;
import com.cfets.ts.s.platform.PlatformHelper;
import com.cfets.ts.s.platform.fetcher.GitlabFetcher;
import com.cfets.ddd.d.platform.repository.domain.ComponentGroup;
import com.cfets.ts.s.platform.git.GitCommander;
import com.cfets.ts.s.platform.gitlab.core.Repositories;
import com.cfets.ts.s.platform.parser.PomFileParser;

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
public class PomFileComponentLoader implements ComponentLoader {

	private PomFileParser pomFileParser = new PomFileParser();

	private GitCommander commander = new GitCommander();

	private GitlabFetcher fetcher = new GitlabFetcher();

	@Override
	public ComponentGroup load(ComponentGroup group) {
		Repositories repos = fetcher.fetch();
		if (repos != null && repos.size() > 0) {
			for (Map.Entry<String, String> entry : repos.entrySet()) {
				String projectName = entry.getKey();
				String sshUrl = PlatformHelper.getRepositorySshUrl(projectName);
				String filePath = ComponentManager.get().getConfig()
						.getBaseUrl();
				// TODO:更新
				File gitFile = new File(filePath + File.separator + projectName
						+ File.separator + ".git");
				if (!gitFile.exists()) {
					commander.clone(projectName, filePath, sshUrl);
				} else {
					commander.pull(projectName, filePath, sshUrl);
				}
			}
			for (Map.Entry<String, String> entry : repos.entrySet()) {
				String projectName = entry.getKey();
				String sshUrl = PlatformHelper.getRepositorySshUrl(projectName);
				String filePath = ComponentManager.get().getConfig()
						.getBaseUrl();
				File pomFile = new File(filePath + File.separator + projectName
						+ File.separator + "pom.xml");
				if(pomFile.exists()){
					group = pomFileParser.readPom(pomFile, group);
				}
			}

		}
		return group;
	}

}
