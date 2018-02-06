package com.cfets.ts.u.platform.job;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabRepositoryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.ts.u.platform.Base64ToStringUtil;
import com.cfets.ts.u.platform.bean.BranchType;
import com.cfets.ts.u.platform.bean.ComponentGroup;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.parser.PomFileParser;
import com.cfets.ts.u.platform.service.ComponentService;
import com.cfets.ts.u.platform.util.SpringUtil;

@Service
public class ChangePomFile extends PublishJob {
	@Autowired
	private ComponentService componentService;
	public static final Logger logger = Logger.getLogger(ChangePomFile.class);
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public void dayOfDelay() {
		long oneDay = 24 * 60 * 60 * 1000;
		// 要延迟的时间，如果是比23：00小，则>0,取剩余的时间，否则就取+1小时的时间
		long initDay = geTimeMills(PlatData.CHANGEPOMFILE)
				- System.currentTimeMillis();
		initDay = initDay > 0 ? initDay : oneDay + initDay;
		logger.info("开始执行定时任务ChangePomFile" + initDay);
		executor.scheduleAtFixedRate(new PushEvent(componentService), initDay,
				oneDay, TimeUnit.MILLISECONDS);
	}

	private long geTimeMills(String time) {
		try {
			Date current = dateFormat.parse(dayFormat.format(new Date()) + " "
					+ time);
			return current.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
}

class PushEvent implements Runnable {
	private static final Logger logger = Logger.getLogger(PushEvent.class);
	private ComponentService componentService;
	public static List<CacheVO> listNtp2release = new ArrayList<CacheVO>();
	public static List<CacheVO> listrelease_108 = new ArrayList<CacheVO>();

	public PushEvent(ComponentService componentService) {
		this.componentService = componentService;
	}

	@Override
	public void run() {

		try {
			GitlabAPI api = GitlabAPI.connect("http://200.31.147.77",
					"4ZJPBCWdTNrJSRoQF6BT");
			// 获取tsgroup
			GitlabGroup group = new GitlabGroup();
			List<GitlabGroup> lll = api.getGroups();
			for (GitlabGroup gg : lll) {
				if ("TS".equals(gg.getName())) {
					group = gg;
				}
			}
			List<GitlabProject> ls = api.getGroupProjects(group.getId());
			// 获取最新版本号
			listNtp2release = getVersionByBranch(api, ls, listNtp2release,
					"ntp2release", "ntp2master");
			/*
			 * listrelease_108 = getVersionByBranch(api, ls, listrelease_108,
			 * "release_1.0.8", "master_1.0.8");
			 */
			// 批量更新数据库
			for (GitlabProject git : ls) {
				File file2 = new File(PlatData.FILEPATH);
				if (!file2.exists()) {
					logger.info("开始初始化读取git信息(文件夹不存在)-----" + git.getName()
							+ "------" + git.getHttpUrl());
					initClone(git.getSshUrl(), git.getName());
				} // 数据的准确性需要
				try {
					GitlabRepositoryFile fileNew = api.getRepositoryFile(git,
							"pom.xml", "ntp2master");
					String pomConent = Base64ToStringUtil.decodeBase64(fileNew
							.getContent()); // todo解析 string
					// --xml 设置到实体中
					parsePomContent(pomConent, BranchType.ntp2master.getType());
				} catch (Exception e) {
					logger.error("{" + git.getName() + "}不存在："
							+ BranchType.ntp2master);
				}

			}
			logger.info("初始化数据完成，开始更新数据batchupdate");
			try {
				batchupdate();
			} catch (Exception e) {
				logger.error(e);
			}
			logger.info("更新数据完成batchChangePom");
			batchChangePom(listNtp2release, api, ls, "ntp2master");
			// batchChangePom(listrelease_108, api, ls,"master_1.0.8");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void batchChangePom(List<CacheVO> lcache, GitlabAPI api,
			List<GitlabProject> ls, String branch) throws Exception {

		for (GitlabProject git : ls) {
			if (!"ts-s-session".equals(git.getName())
					|| !"ts-s-jdbc".equals(git.getName())) {
				continue;
			}
			File file1 = new File(PlatData.FILEPATH + "/" + git.getName());
			File file = new File(PlatData.FILEPATH + "/" + git.getName()
					+ "//.git");

			if (!file1.exists() || !file.exists()) {
				logger.info("文件不存在，暂时不执行");
				continue;
			}
			org.eclipse.jgit.lib.Repository repo = new FileRepositoryBuilder()
					.setGitDir(file).build();
			Git git1 = new Git(repo);
			try {
				git1.pull().call();
			} catch (Exception e2) {
				logger.error("拉取代码异常", e2);
			}
			Map<String, Ref> map = git1.getRepository().getAllRefs();
			Set<String> keys = map.keySet();
			logger.info("------------------------------start-----------------------------");
			for (String one11 : keys) {
				// 目前写死--master，远程分支
				if (one11.indexOf(branch) >= 0
						&& one11.indexOf("refs/remotes") >= 0) {
					branch = one11.substring(one11.lastIndexOf("/") + 1,
							one11.length());
					logger.info("要切换代码分支版本：：" + branch);
					logger.info("当前分支：：" + git1.getRepository().getBranch());
					if (!branch.equals(git1.getRepository().getBranch())) {
						// logger.info("要check分支");
						try {
							git1.branchDelete().setBranchNames(branch)
									.setForce(true).call();
						} catch (Exception e1) {
							logger.error("删除分支异常", e1);
						}
						try {
							CheckoutCommand checkoutCommand = git1.checkout();
							// 设置分支名称
							checkoutCommand.setName(branch);
							// 分支是从那个remote取的代码，默认是master
							checkoutCommand.setStartPoint(one11);
							checkoutCommand.setCreateBranch(true);
							checkoutCommand.call();
						} catch (Exception e) {
							logger.error("checkout失败", e);
						}
					}
					try {
						PullCommand pull = git1.pull();
						PullResult resout = pull.call();
						if (resout.isSuccessful()) {
							String pomPath = PlatData.FILEPATH + File.separator
									+ git.getName() + File.separator
									+ "pom.xml";
							SAXReader reader = new SAXReader();
							Document document = reader.read(pomPath);
							Element rootElement = document.getRootElement();
							Element dependenciesElement = rootElement
									.element("dependencies");
							for (Iterator it = dependenciesElement
									.elementIterator(); it.hasNext();) {
								Element itElement = (Element) it.next();
								Element groupId = itElement.element("groupId");
								Element artifactId = itElement
										.element("artifactId");
								Element version = itElement.element("version");
								String groupString = groupId.getTextTrim();
								String artiString = artifactId.getTextTrim();
								String verString = version.getTextTrim();
								for (CacheVO vo : lcache) {
									if (groupString.equals(vo.getGroupId())
											&& artiString.equals(vo
													.getArtifactId())
											&& "ntp2master".equals(vo
													.getBranch())) {
										version.setText(vo.getVersion());
									}
								}

							}
							OutputFormat format = OutputFormat
									.createPrettyPrint();
							format.setEncoding("utf-8");
							FileOutputStream output = new FileOutputStream(
									pomPath);
							XMLWriter writer = new XMLWriter(output, format);
							writer.write(document);
							writer.flush();
							writer.close();

							try {
								AddCommand add = git1.add();
								add.addFilepattern(".").call();
								CommitCommand commit = git1.commit();
								commit.setCommitter("zhairuiping",
										"zhairuiping@scm.cfets.com");
								commit.setAuthor("zhairuiping",
										"zhairuiping@scm.cfets.com");
								commit.setAll(true);
								RevCommit revCommit = commit.setMessage(
										" 批量更新Pom为最新的版本号").call();
								String commitId = revCommit.getId().name();
								logger.info("commitId:" + commitId);
								PushCommand push = git1.push();
								push.call();
							} catch (Exception e) {
								logger.error("上传版本失败", e);
							}

						}
					} catch (Exception e) {
						logger.error("pull失败", e);
					}

				} else {
					logger.info("不是需要处理的相关分支");
				}
			}
		}

	}

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

	private void batchupdate() {
		int[] cnt = componentService.batchUpdateVersion(listNtp2release);
		logger.warn("批量更新个数：----》" + cnt.length);
	}

	private List<CacheVO> getVersionByBranch(GitlabAPI api,
			List<GitlabProject> ls, List<CacheVO> list, String sourceBranch,
			String targetBranch) {
		for (GitlabProject git : ls) {
			try {
				GitlabRepositoryFile file = api.getRepositoryFile(git,
						"pom.xml", sourceBranch);
				String pomConent = Base64ToStringUtil.decodeBase64(file
						.getContent());
				CacheVO vo = new CacheVO();
				SAXReader reader = new SAXReader();
				Document document = reader.read(new ByteArrayInputStream(
						pomConent.getBytes("utf-8")));

				Element rootElement = document.getRootElement();
				Element artifactElement = rootElement.element("artifactId");
				Element versionElement = rootElement.element("version");
				Element groupIdElement = rootElement.element("groupId");
				String artifactId = artifactElement.getTextTrim();
				String version = versionElement.getTextTrim();
				String groupId = "";
				if (groupIdElement == null) {
					Element parents = rootElement.element("parent");
					groupIdElement = parents.element("groupId");
					groupId = groupIdElement.getTextTrim();
				} else {
					groupId = groupIdElement.getTextTrim();
				}
				vo.setArtifactId(artifactId);
				vo.setGroupId(groupId);
				vo.setBranch(targetBranch);
				vo.setVersion(version);
				list.add(vo);
			} catch (Exception e) {
				logger.info("解析异常" + git.getName());
			}

		}
		return list;
	}

	public static GitlabHTTPRequestor retrieve(GitlabAPI api) {
		return new GitlabHTTPRequestor(api).authenticate(
				PlatData.PRIVATE_TOKEN, TokenType.PRIVATE_TOKEN,
				AuthMethod.HEADER);
	}

}
