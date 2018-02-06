package com.cfets.ts.u.platform.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;



public class BuildBranch {
	private static final Logger logger = Logger
			.getLogger(BuildBranch.class);
	public static void main(String[] args)  {
		try {
			GitlabAPI api = GitlabAPI.connect("http://200.31.147.77",
					"4ZJPBCWdTNrJSRoQF6BT");
			GitlabGroup group = new GitlabGroup();
			List<GitlabGroup> lll = api.getGroups();
			for (GitlabGroup gg : lll) {
				if ("TS".equals(gg.getName())) {
					group = gg;
				}
			}
			List<GitlabProject> ls = api.getGroupProjects(group.getId());
			
			for (GitlabProject git : ls) {
					List<GitlabBranch> branchs=api.getBranches(git);
					for (GitlabBranch gitlabBranch : branchs) {
						if (gitlabBranch.getName().equals("master_1.0.8")) {
							break;
						}else if (gitlabBranch.getName().equals("ntp2release")) {
							api.createBranch(git, "master_1.0.8", "ntp2release");
							api.createBranch(git, "release_1.0.8", "ntp2release");
						}
						
					}
//						System.out.println(git.getName());
//						for (GitlabBranch gitlabBranch : branchs) {
//							System.out.println(gitlabBranch.getName());							
//						}
//						System.out.println();
					
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
