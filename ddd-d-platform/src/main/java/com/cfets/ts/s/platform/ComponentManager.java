/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform;

import java.util.ArrayList;
import java.util.List;

import com.cfets.ts.s.platform.fetcher.GitlabFetcher;
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
public class ComponentManager {

	private PlatformConfig config = new PlatformConfig();

	private ComponentGroup componentGroup = new ComponentGroup();

	private List<ComponentLoader> loaders = new ArrayList<ComponentLoader>();

	public void registerComponent(Component component) {
		componentGroup.registerComponent(component);
	}

	public void registerComponent(List<Component> components) {
		for (Component component : components) {
			componentGroup.registerComponent(component);
		}
	}

	public void registerLoader(ComponentLoader loader) {
		loaders.add(loader);
	}

	public void loadComponent() {
		for (ComponentLoader loader : loaders) {
			loader.load(componentGroup);
		}
	}

	public void init() {
		// 从Gitlab中同步所有工程
		GitlabFetcher gitlabFetcher = new GitlabFetcher();
		gitlabFetcher.fetch();

	}

	// 静态内部类单例实现

	private ComponentManager() {
	}

	private static class ComponentManagerHolder {
		private static final ComponentManager instance = new ComponentManager();
	}

	public static final ComponentManager get() {
		return ComponentManagerHolder.instance;
	}

	public PlatformConfig getConfig() {
		return config;
	}

	public void setConfig(PlatformConfig config) {
		this.config = config;
	}

}
