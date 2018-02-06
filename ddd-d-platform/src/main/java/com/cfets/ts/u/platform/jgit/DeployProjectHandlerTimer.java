package com.cfets.ts.u.platform.jgit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.ts.u.platform.ExploringAxonApplication;
import com.cfets.ts.u.platform.bean.MegerRequestVO;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
import com.cfets.ts.u.platform.service.ComponentService;

/**
 * \ ams 服务的部署
 * 
 * @author zrp
 * 
 */
@Service("deployProjectHandlerTimer")
public class DeployProjectHandlerTimer {
	private static final Logger logger = Logger
			.getLogger(ExploringAxonApplication.class);
	@Autowired
	private ComponentService componentService;

	public Map<String, Object> handle(List<MegerRequestVO> list,
			Map<String, Object> flag) {
		synchronized (this) {
			logger.info("第四个handler（DeployProjectHandler）=========");
			// 做成枚举项
			String amsPath = PlatData.FILEPATH  + File.separator + "ts-dw-ams";
			String dpsPath = PlatData.FILEPATH + File.separator + "ts-dw-dps";
			String dqsPath = PlatData.FILEPATH + File.separator + "ts-dw-dqs";
			// String dxsPath = "/home/ts/app/git/ts-dp-css";
			String fcsPath = PlatData.FILEPATH + File.separator + "ts-dp-fcs";
			String jssPath = PlatData.FILEPATH + File.separator + "ts-dp-jss";
			String mcPath = PlatData.FILEPATH + File.separator + "ts-dp-mc";
			String cxxfxPath = PlatData.FILEPATH + File.separator
					+ "ts-dp-css-fx";
			// String dpdqsPath = "/home/ts/app/git/ts-dp-dqs";
			File targetFile;
			List<String> fileList = new ArrayList();
			fileList.add(amsPath);
			fileList.add(dpsPath);
			fileList.add(dqsPath);
			fileList.add(jssPath);
			fileList.add(fcsPath);
			// fileList.add(dxsPath);
			fileList.add(cxxfxPath);
			fileList.add(mcPath);
			// fileList.add(dpdqsPath);
			logger.info("遍历服务fileList====" + fileList.size());
			Iterator<String> iterator = fileList.iterator();
			while (iterator.hasNext()) {
				// 是否要重新部署
				String path = iterator.next(); // 克隆文件的位置
				logger.info("开始遍历服务:" + path);
				File refile = new File(path + File.separator + ".git");// 当前文件
				org.eclipse.jgit.lib.Repository repo = null;
				try {
					repo = new FileRepositoryBuilder().setGitDir(refile)
							.build();
				} catch (Exception e2) {
					logger.error("org.eclipse.jgit.lib.Repository exception",
							e2);
				}
				Git git = new Git(repo);
				try {
					// 1,拉取最新的代码
					logger.info("1)拉取最新的代码");
					flag = pullNewCode(list, flag, path, git);
					// 2,修改pom文件和打包
					logger.info("2)修改pom文件和打包工程");
					flag = isRunFlag(list, flag, path, git);
					// 3,部署服务 4,提交代码
					logger.info("3)部署服务和4)提交代码");
					flag = deployProject(list, flag, path, git);
				} catch (Exception e) {
					try {
						git.reset().setMode(ResetType.HARD).setRef("HEAD")
								.call();
						flag.put("flag", false);
						flag.put("msg：", "未知异常");
						logger.error("部署项目失败:" + "在工程::" + path, e);
					} catch (Exception e1) {
						logger.error(e1);
					}
				}

			}
			return flag;
		}
	}

	/**
	 * pull代码
	 * 
	 * @param object
	 * @param flag
	 * @param path
	 * @param git
	 * @return
	 */
	private Map<String, Object> pullNewCode(List<MegerRequestVO> list,
			Map<String, Object> flag, String path, Git git) {
		// 是否需要checkout
		boolean isCheckout = true;
		try {
			ListBranchCommand ls = git.branchList();
			File file = new File(path);
			// clone
			try {
				Git.cloneRepository()
						.setURI("http://gitlab.scm.cfets.com/ts/ts-u-dealmgmt")
						.setDirectory(file).call();
			} catch (Exception e) {
				logger.error("代码clone失败,仓库有这个工程不影响进程，如果");
			}
			for (Ref ref : ls.call()) {
				// 本地分支名称
				if ("dev2"
						.equals(ref.getName().replaceFirst("refs/heads/", ""))) {
					try {
						git.branchDelete().setBranchNames("dev2")
								.setForce(true).call();
					} catch (Exception e) {
						isCheckout = false;
						break;
					}
					isCheckout = true;
					break;
				}
			}
			// 如果当前已经是ntpmaster 分支，直接git pull就可以了
			if (isCheckout) {
				// 开始新建本地分支
				CheckoutCommand checkoutCommand = git.checkout();
				// 设置分支名称
				checkoutCommand.setName("dev2");
				// 分支是从那个remote取的代码，默认是master
				checkoutCommand.setStartPoint("refs/remotes/origin/" + "dev2");
				checkoutCommand.setCreateBranch(true);
				checkoutCommand.call();
			}
			// 拉取最新代码-异步的，执行完才往下

			PullCommand pull = git.pull();
			PullResult resout = pull.call();
			logger.info("服务：" + path + ",pull结果：" + resout.isSuccessful());
			// String path = iterator.next();
			if (resout.isSuccessful()) {
				flag.put("flag", true);
				flag.put("msg", "pull代码成功");
			} else {
				flag.put("flag", false);
				flag.put("msg", "pull代码失败");
				flag.put(path, path);
			}
		} catch (Exception e) {
			logger.error("pull代码失败", e);
			// 测试
			flag.put("flag", false);
			flag.put("msg", "pull代码失败");
			flag.put(path, path);
		}
		return flag;

	}

	// 跑服务
	private Map<String, Object> isRunFlag(List<MegerRequestVO> list,
			Map<String, Object> flag, String path, Git git) {
		boolean runflag = false;
		File targetFile = null;
		if (flag.get("flag").equals(true)) {
			try {
				String pomPath = path + File.separator + "pom.xml";
				SAXReader reader = new SAXReader();
				Document document = reader.read(pomPath);
				Element rootElement = document.getRootElement();
				Element dependenciesElement = rootElement
						.element("dependencies");
				for (Iterator it = dependenciesElement.elementIterator(); it
						.hasNext();) {
					Element itElement = (Element) it.next();
					Element artifactId = itElement.element("artifactId");
					Element version = itElement.element("version");
					String artiString = artifactId.getText();
					String verString = version.getText();
					for (MegerRequestVO vo : list) {
						if (artiString.equals(vo.getProjectName())) {
							runflag = true;
							String newversion = vo.getVersion();
							version.setText(newversion);
						}
					}

				}
				if (runflag) {
					logger.info("开始所属工程的pom文件修改");
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("utf-8");
					FileOutputStream output = new FileOutputStream(pomPath);
					XMLWriter writer = new XMLWriter(output, format);
					writer.write(document);
					writer.flush();
					writer.close();
					logger.info("pom文件修改完毕");
					logger.info("========mvn clean install project=====" + path);
					targetFile = new File(path);
					Runtime runtime = Runtime.getRuntime();
					String[] installcmd = { "/bin/sh", "-c",
							"mvn clean install -DskipTests" };
					Process installProcess = runtime.exec(installcmd, null,
							targetFile);
					String outString;
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(
									installProcess.getInputStream()));
					while ((outString = bufferedReader.readLine()) != null) {
						logger.info(outString);
						if (outString.indexOf("[ERROR]") != -1) {
							logger.error("mvn  install代码失败");
							flag.put("flag", false);
							flag.put("msg", "mvn  install代码失败");
							flag.put(path, path);
							return flag;
						}
					}
					installProcess.waitFor();
					flag.put("flag", true);
					flag.put("rootElement", rootElement);
					return flag;
				}
				flag.put("flag", true);
				flag.put("msg", "此工程中不包含当前构件");
				return flag;
			} catch (Exception e) {
				logger.error("未知异常", e);
				flag.put("flag", false);
				flag.put("msg", "未知异常");
				flag.put(path, path);
				return flag;
			}

		}
		return flag;

	}

	/**
	 * 部署和上传代码
	 * 
	 * @param object
	 * @param flag
	 * @param path
	 * @param git
	 * @return
	 */
	private Map<String, Object> deployProject(List<MegerRequestVO> list,
			Map<String, Object> flag, String path, Git git) {
		if (flag.get("flag").equals(true) && flag.get("rootElement") != null) {
			try {
				Element rootElement = (Element) flag.get("rootElement");
				flag.remove("rootElement");
				Runtime runtime = Runtime.getRuntime();
				String outString;
				BufferedReader bufferedReader = null;
				if (path.startsWith("/home/ts/git/ts-dp")) {
					String dpname = rootElement.element("artifactId").getText();
					String dpversion = rootElement.elementText("version");
					logger.info("dpname:" + dpname);
					if (dpname.endsWith("css")) {
						File runFileS = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocessS = runtime.exec(
								"bash shutdown-css.sh", null, runFileS);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocessS.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocessS.waitFor();
						logger.info("停止Dp-css工程");
						File runFile = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocess = runtime.exec(
								"bash startup-css.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocess.waitFor();
						logger.info("启动Dp-css工程");
					} else if (dpname.endsWith("css-fx")) {
						File runFileS = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocessS = runtime.exec(
								"bash shutdown-cssfx.sh", null, runFileS);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocessS.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocessS.waitFor();
						logger.info("停止Dp-css-fx工程");
						File runFile = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocess = runtime.exec(
								"bash startup-cssfx.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocess.waitFor();
						logger.info("启动Dp-css-fx工程");
					} else if (dpname.endsWith("fcs")) {
						File runFileS = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocessS = runtime.exec(
								"bash shutdown-fcs.sh", null, runFileS);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocessS.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocessS.waitFor();
						logger.info("停止Dp-fcs工程");
						File runFile = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocess = runtime.exec(
								"bash startup-fcs.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocess.waitFor();
						logger.info("启动Dp-fcs工程");
					} else if (dpname.endsWith("mc")) {
						File runFileS = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocessS = runtime.exec(
								"bash shutdown-mc.sh", null, runFileS);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocessS.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocessS.waitFor();
						logger.info("停止Dp-mc工程");
						File runFile = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocess = runtime.exec(
								"bash startup-mc.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocess.waitFor();
						logger.info("启动Dp-mc工程");
					} else if (dpname.endsWith("jss")) {
						File runFileS = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocessS = runtime.exec(
								"bash shutdown-jss.sh", null, runFileS);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocessS.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocessS.waitFor();
						logger.info("停止Dp-jss工程");
						File runFile = new File(path + File.separator
								+ "target" + File.separator + dpname + "-"
								+ dpversion + File.separator + "bin");
						Process startprocess = runtime.exec(
								"bash startup-jss.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										startprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						startprocess.waitFor();
						logger.info("启动Dp-jss工程");
					}
					flag.put("flag", true);
					logger.info("开始上传最新构建");
					pushCommit(git, "批量启动");
					logger.info("最新构建上传完成");
					/*
					 * git.reset().setMode(ResetType.HARD)
					 * .setRef("HEAD").call();
					 */
				} else if (path.startsWith("/home/ts/git/ts-dw")) {
					String dpname = rootElement.element("artifactId").getText();
					logger.info("dpname:" + dpname);
					File runFile;
					if (dpname.endsWith("ams")) {
						runFile = new File("/home/ts/dev/tomcat_ams/bin");
						logger.info("停止ams-dw工程");
						Process shutdownprocess = runtime.exec(
								"bash tomcat_ams_shutdown.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										shutdownprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						shutdownprocess.waitFor();
					} else if (dpname.endsWith("dps")) {
						runFile = new File("/home/ts/dev/tomcat_dps/bin");
						logger.info("停止dps-dw工程");
						Process shutdownprocess = runtime.exec(
								"bash tomcat_dps_shutdown.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										shutdownprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						shutdownprocess.waitFor();
					} else {
						runFile = new File("/home/ts/dev/tomcat_dqs/bin");
						logger.info("停止dqs-dw工程");
						Process shutdownprocess = runtime.exec(
								"bash tomcat_dqs_shutdown.sh", null, runFile);
						bufferedReader = new BufferedReader(
								new InputStreamReader(
										shutdownprocess.getInputStream()));
						while ((outString = bufferedReader.readLine()) != null) {
							logger.info(outString);
						}
						shutdownprocess.waitFor();

					}
					logger.info("启动dw工程");
					Process startprocess = runtime.exec("bash startup.sh",
							null, runFile);
					bufferedReader = new BufferedReader(new InputStreamReader(
							startprocess.getInputStream()));
					while ((outString = bufferedReader.readLine()) != null) {
						logger.info(outString);
					}
					startprocess.waitFor();
					logger.info("启动dw工程" + path);
					flag.put("flag", true);
					flag.put("msg", "部署项目成功");
					// 上传最新的pom依赖
					logger.info("开始上传最新构建");
					pushCommit(git, "批量启动");
					logger.info("最新构建上传完成");

				}
			} catch (Exception e) {
				logger.error("未知异常", e);
				flag.put("flag", false);
				flag.put("msg", "未知异常");
				try {
					git.reset().setMode(ResetType.HARD).setRef("HEAD").call();
				} catch (Exception e1) {
					logger.error("回退代码失败", e);
				}
			}
		}
		return flag;
	}

	/*
	 * public static void main(String[] args) throws Exception { Map<String,
	 * Object> map = new HashMap<String, Object>(); String pomPath =
	 * "C://Users//zrp//git//ts-s-log" + File.separator + "pom.xml"; SAXReader
	 * reader = new SAXReader(); Document document = reader.read(pomPath);
	 * Element rootElement = document.getRootElement();
	 * map.put("rootElement",rootElement);
	 * System.out.println(map.get("rootElement")!=null); Element rootElement13 =
	 * (Element) map.get("rootElement"); map.remove("rootElement"); String
	 * dpname = rootElement13.element("artifactId").getText(); String dpversion
	 * = rootElement13.elementText("version"); System.out.println(dpname); }
	 */
	private void pushCommit(Git git, String title) throws Exception {
		try {
			AddCommand add = git.add();
			add.addFilepattern(".").call();
			CommitCommand commit = git.commit();
			commit.setCommitter("zhairuiping", "zhairuiping@scm.cfets.com");
			commit.setAuthor("zhairuiping", "zhairuiping@scm.cfets.com");
			commit.setAll(true);
			RevCommit revCommit = commit
					.setMessage(" dev2 user jgit::" + title).call();
			String commitId = revCommit.getId().name();
			logger.info("commitId:" + commitId);
			PushCommand push = git.push();
			push.call();
		} catch (Exception e) {
			logger.error("上传版本失败", e);
		}

	}

	/*
	 * public static void main(String[] args) { Map<String,String> map = new
	 * HashMap<String,String>(); map.put("aa", "aaa"); map.put("bb", "bbb");
	 * map.put("cc", "bbb"); System.out.println(map.toString()); }
	 */
	/*
	 * public static void main(String[] args) { try { Runtime runtime =
	 * Runtime.getRuntime(); File runFile = new
	 * File("C://Users//zrp//Desktop//tomcat//tomcat_dqs//bin");
	 * logger.info("启动dw工程"); String[] cmd ={ "cmd","/C","startup.bat" };
	 * Process startprocess = runtime.exec( cmd, null, runFile);
	 * //System.out.println(startprocess.exitValue()); BufferedReader
	 * bufferedReader = new BufferedReader( new InputStreamReader(
	 * startprocess.getInputStream())); String outString=""; while ((outString =
	 * bufferedReader.readLine()) != null) { logger.info(outString); } int a
	 * =startprocess.waitFor(); System.out.println(a); } catch (Exception e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } }
	 */
	/*
	 * public static void main(String[] args) throws Exception{ String filePath
	 * = "C://Users//zrp//git"; String ntp2masterBranch ="ntp2master";//
	 * 获取merge的目标分支 File refile = new File(filePath + File.separator +
	 * "ts-s-resource" + File.separator + ".git");// 当前文件 // 创建本地仓库 // todo
	 * 如果是新工程就clone org.eclipse.jgit.lib.Repository repo = new
	 * FileRepositoryBuilder() .setGitDir(refile).build(); Git git = new
	 * Git(repo); PushCommit(git); }
	 */
}

/*
 * public static void main(String[] args) throws Exception { String pomPath =
 * "C://Users//zrp//git//ts-u-cashflow//pom.xml"; SAXReader reader = new
 * SAXReader(); Document document = reader.read(pomPath); Element rootElement =
 * document.getRootElement(); Element dependenciesElement =
 * rootElement.element("dependencies"); for (Iterator it =
 * dependenciesElement.elementIterator(); it.hasNext();) { Element itElement =
 * (Element) it.next(); Element artifactId = itElement.element("artifactId");
 * Element version = itElement.element("version"); String artiString =
 * artifactId.getTextTrim(); if (artiString.equals("ts-s-rate")) { String
 * newversion = "中国"; version.setText(newversion); } }
 * 
 * // 继续验证========= OutputFormat format = OutputFormat.createPrettyPrint();
 * format.setEncoding("utf-8"); FileOutputStream output = new
 * FileOutputStream(pomPath); XMLWriter writer = new XMLWriter(output, format);
 * writer.write(document); writer.flush(); writer.close();
 * 
 * }
 */

