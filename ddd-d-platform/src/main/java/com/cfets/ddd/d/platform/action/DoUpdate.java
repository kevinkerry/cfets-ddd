package com.cfets.ddd.d.platform.action;

import java.util.Set;

import com.cfets.ts.s.platform.bean.DependencyBean;
import com.cfets.ts.s.platform.util.PomVersionExplorer;

public class DoUpdate {
	
	public void updateFromRepo(Set<DependencyBean> dependencyBeans){
		doUpdate(dependencyBeans);
	}
	
	private Set<DependencyBean> doUpdate(Set<DependencyBean> dependencyBeans){
		for (DependencyBean dependencyBean : dependencyBeans) {
			String latestVersionFromRepo = PomVersionExplorer.getLatestVersionFromRepo(dependencyBean.getGroupId(),
					dependencyBean.getArtifactId());
			if (!dependencyBean.getVersion().equals(latestVersionFromRepo)) {
				System.out.println(dependencyBean.toString() + "将要更新，最新版本为：" + latestVersionFromRepo);
				dependencyBean.setVersion(latestVersionFromRepo);
			}
		}
		return dependencyBeans;
	}

}
