package com.cfets.ts.u.platform.service;

import java.io.IOException;

import org.gitlab.api.GitlabAPI;

import com.cfets.ts.u.platform.contants.PlatData;

public class main2 {

	public static void bb(String[] args) {
		GitlabAPI api = null;
		// project,merge id ,iid
		api = GitlabAPI.connect(PlatData.HTTPPATH,
				PlatData.PRIVATE_TOKEN);
		try {
			api.updateMergeRequest(312,13399,"release_1.0.9", 204,
					"rrrrr", "ttttt", "", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
