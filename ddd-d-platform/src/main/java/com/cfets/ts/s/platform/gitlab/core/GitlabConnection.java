/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.gitlab.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabProjectMember;
import org.gitlab.api.models.GitlabSession;

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
public class GitlabConnection {
	
	private static final Logger logger = Logger.getLogger(GitlabConnection.class);

	public final String host;
	public final GitlabSession session;
	public final GitlabProject project;
	// public final GitlabAttributeMapper mapper;

	private List<GitlabMilestone> milestones;
	private List<GitlabProjectMember> members;

	public GitlabConnection(String host, GitlabProject project,
			GitlabSession session) {
		this.host = host;
		this.project = project;
		this.session = session;
		// this.mapper = mapper;
	}

	public GitlabAPI api() {
		return GitlabAPI.connect(host, session.getPrivateToken());
	}

	public void update() throws IOException {
		ArrayList<GitlabProjectMember> memberList = new ArrayList<GitlabProjectMember>();

		milestones = api().getMilestones(project);
		memberList.addAll(api().getProjectMembers(project));
		// This might fail sometimes, because the namespace is not an actual
		// namespace.
		// If the "namespace" is a user namespace, it will fail
		try {
			memberList
					.addAll(api().getNamespaceMembers(project.getNamespace()));
		} catch (Exception e) {
		} catch (Error e) {
		}
		members = Collections.unmodifiableList(memberList);
	}

	public List<GitlabMilestone> getMilestones() {
		return Collections.unmodifiableList(milestones);
	}

	public List<GitlabProjectMember> getProjectMembers() {
		return Collections.unmodifiableList(members);
	}

}
