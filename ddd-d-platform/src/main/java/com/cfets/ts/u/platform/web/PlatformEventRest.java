
package com.cfets.ts.u.platform.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cfets.cwap.s.util.annotation.Note;
import com.cfets.ts.u.platform.api.commond.MergeCommond;
import com.cfets.ts.u.platform.service.PlatformService;
import com.google.gson.Gson;

@Controller
public class PlatformEventRest {
   
	private static final Logger logger = Logger
			.getLogger(PlatformEventRest.class);
	public static ExecutorService executor = Executors.newFixedThreadPool(4);
	@Autowired
	private PlatformService platformService;
	

	

	@Note(note = "监听merge request事件")
	@RequestMapping("/merge2")
	@ResponseBody
	public String getAllChannelStates(@RequestBody String json) {
		logger.info(json + "监听到的消息---" + "当前的类加载器---"
				+ this.getClass().getClassLoader());
		// @RequestBody

		if (StringUtils.isEmpty(json)) {
			logger.info("参数为空");
			return "参数为空";
		} 

		final MergeCommond commond = new Gson().fromJson(json,
				MergeCommond.class);
		try {
			logger.info(commond.getObject_attributes().getId()
					+ "--解析json之后的消息mergeId");
			logger.info(commond.getObject_attributes().getTarget_project_id()
					+ "--解析json之后的消息target/projectId");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String id = commond.getObject_attributes().getId();
		/*
		 * final MergeCommond commond = new MergeCommond();
		 * commond.setObject_kind("merge_request"); MergeObejectEntity
		 * object_attributes = new MergeObejectEntity();
		 * object_attributes.setId("9898");
		 * object_attributes.setTitle("#这是我第一次提交");
		 * object_attributes.setTarget_branch("release1");
		 * object_attributes.setDescription("#鹿晗很帅");
		 * object_attributes.setTarget_project_id(9898);
		 * commond.setObject_attributes(object_attributes); User user = new
		 * User(); user.setName("luhan"); user.setUsername("luhan");
		 * commond.setUser(user); final String id
		 * =commond.getObject_attributes().getId();
		 */

	/*	if ("push".equals(commond.getObject_kind())) {
			logger.info("push 请求不处理");
			return "push 请求不处理";
		}*/
		executor.execute(new PlatFormMessageOne(commond,platformService));
		return "处理结束";

	}
}
