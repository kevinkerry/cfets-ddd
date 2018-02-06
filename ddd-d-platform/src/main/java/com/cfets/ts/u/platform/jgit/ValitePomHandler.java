package com.cfets.ts.u.platform.jgit;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.ts.s.platform.bean.DependencyBean;
import com.cfets.ts.s.platform.util.PomParser;
import com.cfets.ts.s.platform.util.PomVersionExplorer;
import com.cfets.ts.s.platform.util.VersionUtil;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
import com.cfets.ts.u.platform.service.ComponentService;

/**
 * pom文件的验证
 * 
 * @author zrp
 * 
 */
@Service("valitePomHandler")
public class ValitePomHandler extends CodeHandler {
	private static final Logger logger = Logger
			.getLogger(ValitePomHandler.class);
	@Autowired
	private ComponentService componentService;

	@Override
	public Map<String, Object> handle(MergeObejectEntity object,
			Map<String, Object> flag) {
		if (getSuccessor() != null) {
			logger.info("第二个handler（ValitePomHandler）=========");
			logger.info("Pom验证开始");
			String filePath = PlatData.FILEPATH;
			String targetFile = filePath + File.separator
					+ object.getTarget().getName();
			File pom = new File(targetFile + File.separator + "pom.xml");
			String ntp2matsterversion;
			String releaseversion;
			Boolean resultBoolean = true;

			try {
				SAXReader reader = new SAXReader();
				Document document = reader.read(pom);
				Element rootElement = document.getRootElement();
				Element versionElement = rootElement.element("version");
				Element parElement = rootElement.element("parent");
				Element groupElement = parElement.element("groupId");
				String groupId = groupElement.getText();
				ntp2matsterversion = versionElement.getText();

				releaseversion = PomVersionExplorer.getLatestVersionFromRepo(
						groupId, object.getTarget().getName());
				if (releaseversion.equals("")) {
					logger.error("maven仓库中没有此构建");
					releaseversion = "0.0.0.0";

				}
				logger.info("============pom版本" + ntp2matsterversion);
				logger.info("============仓库中版本" + releaseversion);
				int result = VersionUtil.compareVersion(ntp2matsterversion,
						releaseversion);
				logger.info("版本号比较结果" + result);

				if (result <= 0) {
					resultBoolean = false;
					flag.put("flag", false);
					flag.put("msg", "版本号大小不对");
					return flag;
				}

				// 验证scope 加一个判断test的去掉
				PomParser pomParser = new PomParser();
				pomParser.readPom(targetFile + File.separator + "pom.xml");
				Map<DependencyBean, String> dependencyBeanMap = pomParser
						.getDependencyBeanMap();
				for (Map.Entry<DependencyBean, String> dependencyBeanEntry : dependencyBeanMap
						.entrySet()) {
					if (dependencyBeanEntry.getKey().getArtifactId()
							.startsWith("ts-s")
							&& !(dependencyBeanEntry.getKey().getScope()
									.equals("provided") || dependencyBeanEntry
									.getKey().getScope().equals("test"))) {
						resultBoolean = false;
						flag.put("flag", false);
						logger.error(dependencyBeanEntry.getKey()
								.getArtifactId() + "依赖scope不符合规范");
						flag.put("msg", dependencyBeanEntry.getKey()
								.getArtifactId() + "Scope错误");
					}
				}
				if (resultBoolean) {
					flag.put("flag", true);
					flag.put("msg", object.getTarget().getName() + "Pom验证通过");
					flag.put("newVersion", ntp2matsterversion);
					logger.info(object.getTarget().getName() + "Pom验证通过");
					flag = getSuccessor().handle(object, flag);

				} else {
					logger.error("pom验证没有通过");
					flag.put("flag", false);
					flag.put("msg", "pom验证没有通过,Scope错误");
					return flag;
				}
			} catch (Exception e) {
				logger.error("pom验证没有通过", e);
				flag.put("flag", false);
				flag.put("msg", "pom验证没有通过,scope错误");
			}
		}
		return flag;
	}

}
