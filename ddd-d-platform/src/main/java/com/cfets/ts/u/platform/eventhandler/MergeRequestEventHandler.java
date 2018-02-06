package com.cfets.ts.u.platform.eventhandler;/*package com.cfets.ts.u.platform.eventhandler;

import org.apache.log4j.Logger;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Component;

import com.cfets.ts.u.platform.api.event.MergeRequestEvent;

@Component
public class MergeRequestEventHandler {
	private static final Logger logger = Logger.getLogger(MergeRequestEventHandler.class);
	public MergeRequestEventHandler() {
	}

	@EventHandler
	public void acceptRequest(MergeRequestEvent event) {
		logger.info("------------开始处理event----------------");
		MergeObject mergeObject = event.getMergeObject();
		try {
			GitlabAPI api = GitlabAPI.connect(
					"http://gitlab.scm.cfets.com/u/zhairuiping",
					"4ZJPBCWdTNrJSRoQF6BT");
			String mergeCommitMessage = "";
			GitlabProject project = new GitlabProject();
			project.setId(Integer.valueOf(mergeObject.getId()));
			project.setDefaultBranch("master");
			Integer mergeRequestId = Integer.valueOf(mergeObject.getId());
			api.acceptMergeRequest(project, mergeRequestId, mergeCommitMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
*/