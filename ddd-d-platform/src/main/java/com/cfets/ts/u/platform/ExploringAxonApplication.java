package com.cfets.ts.u.platform;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabRepositoryFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import com.cfets.ts.u.platform.bean.BranchType;
import com.cfets.ts.u.platform.bean.ComponentGroup;
import com.cfets.ts.u.platform.bean.User;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.job.ChangePomFile;
import com.cfets.ts.u.platform.job.CommitEventJob;
import com.cfets.ts.u.platform.job.CopyOfCommitEventJob;
import com.cfets.ts.u.platform.job.DemoEvent;
import com.cfets.ts.u.platform.job.TimeDeployeeJobOne;
import com.cfets.ts.u.platform.job.TimeDeployeeJobTwo;
import com.cfets.ts.u.platform.parser.PomFileParser;
import com.cfets.ts.u.platform.service.ComponentServiceImpl;
import com.cfets.ts.u.platform.util.SpringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 1,自动merge（ntp2master,master_1.0.8）工作（审核和审批）--完成 2,定时统计工作量（每天晚上23:30）---完成
 * 3，定时更新pom文件----未完成 4，构件任务计划，展示发布信息--未完成 5，构件之间的相互关系入库--完成
 * 6，展示构件关系--ts-s-platform--完成 7，自动创建分支代码整合在工程里--未完成 8，自动创建hook代码---未完成
 * 
 * @author zrp
 * 
 */
// 区分所有的分支:1.1.0服务
@SpringBootApplication
public class ExploringAxonApplication {
	// 配置日志文件
	private static final Logger logger = Logger
			.getLogger(ExploringAxonApplication.class);
	public static Map<String, Integer> map = new HashMap<String, Integer>();

	public static void main(String[] args) {
		logger.error("服务启动开始-----");
		SpringApplication.run(ExploringAxonApplication.class, args);
		// initProperties();
		// 创建异步线程
	/*	new Thread(new Runnable() {
			@Override
			public void run() {
				init();
			}

		}, "initData").start();*/
		logger.info("服务启动成功-----");
		CommitEventJob job = SpringUtil.getBean(CommitEventJob.class);
		job.dayOfDelay();
	  
	/*	CopyOfCommitEventJob c =SpringUtil.getBean(CopyOfCommitEventJob.class);
		c.dayOfDelay();*/
		//new Thread(SpringUtil.getBean(DemoEvent.class)).start();
	}

	private static void init() {
		File file1 = new File(PlatData.FILEPATH);
		logger.info("开始初始化pom数据-----");
		// 获取连接
		try {
			GitlabAPI api = GitlabAPI.connect(PlatData.HTTPPATH,
					PlatData.PRIVATE_TOKEN);
			// 获取group
			GitlabGroup group = new GitlabGroup();
			List<GitlabGroup> lll = api.getGroups();
			for (GitlabGroup gg : lll) {
				if ("TS".equals(gg.getName())) {
					group = gg;
				}
			}
			// List<GitlabGroupMember> lg =
			// api.getGroupMembers(group);//此方法有缺陷，目前暂时不用死循环

			/*
			 * for(GitlabGroupMember gg:lg){ System.out.println(gg.getName()); }
			 */
			// 获取用户列表
			String tailUrl = GitlabGroup.URL + "/" + group.getId()
					+ "/members/";

			String json = new String(retrieve(api).to(tailUrl, byte[].class));
			List<User> ls1 = new Gson().fromJson(json,
					new TypeToken<List<User>>() {
					}.getType());
			/*
			 * for (User user : ls1) { map.put(user.getUsername(),
			 * user.getId()); }
			 */
			// 新增数据
			logger.info("开始新增用户-----" + ls1.size());
			try {
				ComponentServiceImpl componentServiceImpl = SpringUtil
						.getBean(ComponentServiceImpl.class);
				componentServiceImpl.save(ls1);
			} catch (Exception e) {
				logger.error("保存异常", e);
			}
			// todo 如何取得所有的project
	/*		List<GitlabProject> ls = api.getGroupProjects(group.getId());
			logger.info("如何取得所有的project size:" + ls.size());
			for (GitlabProject git : ls) {
				 if (!file1.exists()) {
				logger.info("开始初始化读取git信息(文件夹不存在)-----" + git.getName()
						+ "------" + git.getHttpUrl());
				initClone(git.getSshUrl(), git.getName());
				 } // 数据的准确性需要
				try {
					GitlabRepositoryFile file = api.getRepositoryFile(git,
							"pom.xml", "ntp2master");
					String pomConent = Base64ToStringUtil.decodeBase64(file
							.getContent()); // todo解析 string
					// --xml 设置到实体中
					parsePomContent(pomConent, BranchType.ntp2master.getType());
				} catch (Exception e) {
					logger.error("{" + git.getName() + "}不存在："
							+ BranchType.ntp2master);
				}
				// 获取文件
				try {
					GitlabRepositoryFile file = api.getRepositoryFile(git,
							"pom.xml", "master");
					String pomConent = Base64ToStringUtil.decodeBase64(file
							.getContent()); // todo解析 string
					// --xml 设置到实体中
					parsePomContent(pomConent, BranchType.master.getType());
				} catch (Exception e) {
					logger.error("{" + git.getName() + "}不存在："
							+ BranchType.master);
				}
				try {
					GitlabRepositoryFile file = api.getRepositoryFile(git,
							"pom.xml", "master2");
					String pomConent = Base64ToStringUtil.decodeBase64(file
							.getContent()); // todo解析 string
					// --xml 设置到实体中
					parsePomContent(pomConent, BranchType.master2.getType());
				} catch (Exception e) {
					logger.error("{" + git.getName() + "}不存在："
							+ BranchType.master2);
				}

			}*/

			// 保存入库

		} catch (Exception e) {
			logger.error("未知异常", e);

		}
		logger.info("初始化pom数据完成-----");

	}

	// 1.1.0git 默认是master分支，如何改为ntp2分支？？？？？？？？？
	private static void initClone(String sshUrl, String gitName) {
		logger.info("初始化克隆数据开始-----");
		try {
			File file = new File(PlatData.FILEPATH + File.separator + gitName);
			if (!file.exists()) {
				Git.cloneRepository().setURI(sshUrl).setDirectory(file).call();
			}

		} catch (Exception e) {
			logger.error("初始化clone数据失败-----", e);
		}

	}

	private static void parsePomContent(String pomConent, String branchType) {
		try {
			PomFileParser parse = SpringUtil.getBean(PomFileParser.class);
			ComponentGroup group = new ComponentGroup();
			parse.readPom(pomConent, group, branchType);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static GitlabHTTPRequestor retrieve(GitlabAPI api) {
		return new GitlabHTTPRequestor(api).authenticate(
				PlatData.PRIVATE_TOKEN, TokenType.PRIVATE_TOKEN,
				AuthMethod.HEADER);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setPort(18092);
		factory.setSessionTimeout(10, TimeUnit.MINUTES);
		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
				"/notfound.html"));
		return factory;
	}
}
