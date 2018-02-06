/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.git;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;

import com.jcraft.jsch.Session;

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
public class GitCommander {

	private static final Logger logger = Logger.getLogger(GitCommander.class);

	public void clone(String project, String filePath, String sshUrl) {
		logger.debug("git clone[" + sshUrl + "]工程[" + project + "]到服务器文件目录["
				+ filePath + "]下");
		// String sshUrl = "git@gitlab.scm.cfets.com:TS/ts-u-crv.git";
		final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
			@Override
			protected void configure(Host hc, Session session) {
				session.setPassword("zhairuiping");// 设置ssh密码
			}
		};

		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setURI(sshUrl)
				.setDirectory(new File(filePath + File.separator + project))
				.setTransportConfigCallback(new TransportConfigCallback() {
					@Override
					public void configure(Transport transport) {
						SshTransport sshTransport = (SshTransport) transport;
						sshTransport.setSshSessionFactory(sshSessionFactory);

					}
				});
		try {
			cloneCommand.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pull(String project, String filePath, String sshUrl) {
		logger.debug("git pull[" + sshUrl + "]工程[" + project + "]到服务器文件目录["
				+ filePath + "]下");
		// String sshUrl = "git@gitlab.scm.cfets.com:TS/ts-u-crv.git";
		try {
			Git git = Git.open(new File(filePath + File.separator + project
					+ File.separator + ".git"));
			git.pull();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
