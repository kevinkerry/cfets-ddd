package com.cfets.ts.u.platform.util;

import org.apache.commons.lang.StringUtils;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;

import com.cfets.ts.u.platform.contants.PlatData;

public class GitLabUtil {/*
public static GitlabAPI getGitlabAPI(String projectId,String mergeId){
	GitlabAPI api=null;
	try {
		api = GitlabAPI.connect(PlatData.HTTPPATH, PlatData.PRIVATE_TOKEN);
		String mergeCommitMessage = "See merge request";
		GitlabProject project = new GitlabProject();
		// project.setSshUrl("git@gitlab.scm.cfets.com:TS/ts-u-platform.git");
		project.setSshUrl(project.getSshUrl());
		project.setDescription("第一次merge---");
		project.setId(mergeObject.getTarget_project_id());
		project.setDefaultBranch("master");
		Integer mergeRequestId = Integer.valueOf(mergeObject.getId());
		logger.info("project id :" + project.getId());
		logger.info("mergequest id :" + mergeRequestId);
		// 合并代码
		api.acceptMergeRequest(project, mergeRequestId, mergeCommitMessage);
	} catch (Exception e) {
		logger.error("accept merge fail" + e.getMessage());
		if (StringUtils.isNotEmpty(e.getMessage())) {
			if (e.getMessage().contains("Branch cannot be merged")) {
				logger.error("代码存在冲突  Branch cannot be merged");
				mergeRequest.setDescription("代码合并存在冲突，请处理");
				try {
					componentService.update(mergeRequest);
				} catch (Exception e1) {
					logger.error("更新失败", e1);
				}
				return;
			}
		}
	}
}
*/}
