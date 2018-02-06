package com.cfets.ts.s.platform;

import javax.annotation.PostConstruct;

import com.cfets.cwap.s.spi.GenericComponent;
import com.cfets.cwap.s.spi.bean.SessionAccount;
import com.cfets.cwap.s.spi.resource.Resource;

/**
 * 资源注册
 * 
 * @author lijian
 * @since 2.3.2.0
 */
@org.springframework.stereotype.Component(PlatformHelper.PLUGIN_ID)
public class PlatformComponent extends GenericComponent {

	@Override
	@PostConstruct
	public void init() {
		// 注册构件
		this.registerComponent();
		// 注册URL资源
		 this.regUrlResources();
		// 拷贝JSP资源到WebInf目录下
	   this.copyJspToWebInf(PlatformComponent.class);
		// 注册国际化资源
		// this.registerI18nResource();
		this.initComponentData();
	}

	private void regUrlResources() {

		String plugin = PlatformHelper.PLUGIN_ID;
		// 数据字典
		this.registerResource(Resource.blank().type(RES_TYPE_URL)
				.key(plugin + ".platformload").label("加载pom文件").value(plugin + "/load.do")
				.plugin(plugin).roles(SessionAccount.ROLE_SYS_GLOBE));
		this.registerResource(Resource.blank().type(RES_TYPE_URL)
				.key(plugin + ".platformindex").label("构件列表")
				.value(plugin + "/index.do").plugin(plugin)
				.roles(SessionAccount.ROLE_SYS_ADMIN));
		this.registerResource(Resource.blank().type(RES_TYPE_URL)
				.key(plugin + ".platformview").label("查询列表")
				.value(plugin + "/view.do").plugin(plugin)
				.roles(SessionAccount.ROLE_SYS_ADMIN));
		this.registerResource(Resource.blank().type(RES_TYPE_URL)
				.key(plugin + ".platformquery").label("查询构件依赖")
				.value(plugin + "/query.do").plugin(plugin)
				.roles(SessionAccount.ROLE_SYS_ADMIN));
	}

	@Override
	public String getPluginId() {
		return PlatformHelper.PLUGIN_ID;
	}

	private void initComponentData() {
		// 简单加载器
		//ComponentManager.get().registerLoader(new SimpleComponentLoader());
		// POM文件加载器
		//ComponentManager.get().registerLoader(new PomFileComponentLoader());
	}

}
