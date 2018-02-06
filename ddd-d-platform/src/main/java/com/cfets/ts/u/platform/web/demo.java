package com.cfets.ts.u.platform.web;/*package com.cfets.ts.u.platform.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.cfets.ts.s.platform.bean.DependencyBean;
import com.cfets.ts.s.platform.bean.ElementBean;
import com.cfets.ts.s.platform.util.PomParser;
import com.cfets.ts.s.platform.util.PomVersionExplorer;
import com.cfets.ts.s.platform.util.VersionUtil;

public class demo {
public static void main(String[] args) {
	String groupId = "com.cfets.ts";
	String artifactId = "ts-s-log";
	String amsPath = "C:\\workspace\\ts-dp-ams";

	String filePath = "C:\\Users\\Administrator\\Desktop\\" + artifactId; // 克隆的文件
	String webString = "git@gitlab.scm.cfets.com:TS/" + artifactId + ".git";
	File file = new File(filePath);
	File refile = new File(filePath + File.separator + ".git");
	String releaseBranch = "release";
	String ntp2masterBranch = "ntp2master";
	File amsfile = new File(amsPath);

	try {

		// clone 项目
		org.eclipse.jgit.lib.Repository repo = new FileRepositoryBuilder()
				.setGitDir(refile).build();
		Git git = new Git(repo);
		Git.cloneRepository().setURI(webString).setDirectory(file).call();

		// 将提交版本与release版本比较

		CheckoutCommand checkoutCommand = git.checkout();
		checkoutCommand.setName("refs/remotes/origin/" + releaseBranch)
				.call();

		String ntp2matsterversion = "";
		String releaseversion = "";
		PomParser pomParser = new PomParser();
		pomParser.readPom(filePath + File.separator + "pom.xml");

		List<ElementBean> beforeDependencyBeans = pomParser
				.getBeforeDependencyBeans();
		Iterator<ElementBean> iterator = beforeDependencyBeans.iterator();

		int tag = 0;
		// 拿gitlab上的发布版本
		// while (iterator.hasNext()) {
		// ElementBean elementBean = (ElementBean) iterator.next();
		// if (elementBean.getValue().equals("version")) {
		// releaseversion = iterator.next().getValue();
		// tag++;
		// }
		// if (tag == 3) {
		// break;
		// }
		//
		// }
		// System.out.println(releaseversion);

		// 拿maven仓库的发布版本
		releaseversion = PomVersionExplorer.getLatestVersionFromRepo(
				groupId, artifactId);
		// 获取提交的版本
		git.checkout().setName("refs/remotes/origin/" + ntp2masterBranch)
				.call();

		pomParser.readPom(filePath + File.separator + "pom.xml");
		beforeDependencyBeans = pomParser.getBeforeDependencyBeans();
		Iterator<ElementBean> iterator2 = beforeDependencyBeans.iterator();
		System.out.println("=======================");

		tag = 0;
		while (iterator2.hasNext()) {
			ElementBean elementBean = (ElementBean) iterator2.next();

			if (elementBean.getValue().equals("version")) {
				ntp2matsterversion = iterator2.next().getValue();
				tag++;
			}
			if (tag == 3) {

				break;
			}

		}

		System.out.println(ntp2matsterversion);
		int result = VersionUtil.compareVersion(ntp2matsterversion,
				releaseversion);
		// System.out.println(result);

		// 验证ts-s是否权威provider

		Map<DependencyBean, String> dependencyBeanMap = pomParser
				.getDependencyBeanMap();
		for (Map.Entry<DependencyBean, String> dependencyBeanEntry : dependencyBeanMap
				.entrySet()) {
			if (dependencyBeanEntry.getKey().getArtifactId()
					.startsWith("ts-s")
					&& !dependencyBeanEntry.getKey().getScope()
							.equals("provided")) {
				return dependencyBeanEntry.getKey().getArtifactId()
						+ "scope不对"
						+ dependencyBeanEntry.getKey().getScope();
			}
		}

		// 如果通过版本验证，本地构建
		if (result != 0) {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(
					"cmd /c mvn clean install -DskipTests", null, file);
			String outString;
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			while ((outString = bufferedReader.readLine()) != null) {
				System.out.println(outString);

			}
			process.waitFor();
		}

		// //修改ams工程中次依赖的版本号
		// update(amsfile, artifactId, ntp2matsterversion);

		// 发布ams工程
		Runtime runtime = Runtime.getRuntime();
		// Process process = runtime.exec(
		// "cmd /c mvn clean install -DskipTests", null, amsfile);
		// String outString;
		// BufferedReader bufferedReader = new BufferedReader(
		// new InputStreamReader(process.getInputStream()));
		// while ((outString = bufferedReader.readLine()) != null) {
		// System.out.println(outString);
		//
		// }
		// process.waitFor();

		// 启动服务
		File startFile = new File(amsPath
				+ "\\target\\ts-dp-ams-0.1.0.0\\bin");
		Process startprocess = runtime.exec(
				"cmd /c start startup-demo.bat", null, startFile);
		startprocess.waitFor();
	} catch (Exception e) {
		System.out.println(e);
	}

	return "call successfully";
}

// 修改版本号
public static void update(File file, String projectname, String newversion)
		throws DocumentException, IOException {
	File amsFile = file;
	SAXReader reader = new SAXReader();
	Document document = reader.read(amsFile);
	Element rootElement = document.getRootElement();
	Element dependencies = rootElement.element("dependencies");
	for (Iterator iterator = dependencies.elementIterator(); iterator
			.hasNext();) {
		Element itElement = (Element) iterator.next();
		Element artifactId = itElement.element("artifactId");
		Element version = itElement.element("version");
		String artiString = artifactId.getText();
		String verString = version.getText();
		if (artiString.equals("projectname")) {
			System.out.println(verString);
			version.setText(newversion);
			verString = version.getText();
			System.out.println(verString);
		}
		FileWriter fwFileWriter = new FileWriter(amsFile);
		org.dom4j.io.XMLWriter writer = new org.dom4j.io.XMLWriter(
				fwFileWriter);
		writer.write(document);
		fwFileWriter.close();
	}
}


 * public static void main(String[] args) { String json =
 * "{\"object_kind\": \"merge_request\",\"object_attributes\":{\"target_branch\":\"zhairuiping\",\"source_branch\":\"http://www.gravatar.com/avatar/f673fd1188b7e7c57be161d4748551fc?s=80\u0026d=identicon\",\"source\": {\"name\": \"ts-u-platform\"}}}"
 * ; MergeCommond commond = new Gson().fromJson(json, MergeCommond.class);
 * System.out.println(commond); }
 
}
}
*/