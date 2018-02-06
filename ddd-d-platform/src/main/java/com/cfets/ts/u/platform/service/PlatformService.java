package com.cfets.ts.u.platform.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabMergeRequest;
import org.gitlab.api.models.GitlabProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.ts.u.platform.api.commond.MergeCommond;
import com.cfets.ts.u.platform.bean.BranchType;
import com.cfets.ts.u.platform.bean.MegerRequestPO;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.jgit.CodeHandler;
import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
import com.cfets.ts.u.platform.util.DateUtils;

@Service("platformService")
public class PlatformService {
	private static final Logger logger = Logger
			.getLogger(PlatformService.class);
	private Queue queue = new LinkedList<MergeObejectEntity>();
	@Autowired
	CodeHandler cloneProiectHandler;
	@Autowired
	CodeHandler valitePomHandler;
	@Autowired
	CodeHandler mavenHandler;
	@Autowired
	CodeHandler deployProjectHandler;
	@Autowired
	private ComponentService componentService;
	private MergeCommond recommond;
	Map<String, Object> mapflagInit = new HashMap<String, Object>();
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void dealMergeAccept(MergeCommond commond) {
		recommond = commond;
		MergeObejectEntity mergeObject = commond.getObject_attributes();
		MegerRequestPO mergeRequest = new MegerRequestPO();
		// 版本计划入库
		// 初始数据入库 ，包括master和release事件，merger过的事件不要
		// 1,过滤：，merger过的事件不要，满足提交的
		// can_be_merged是点完accept的值
		if ("opened".equals(mergeObject.getState())
				&& ("unchecked".equals(mergeObject.getMerge_status()) || "can_be_merged"
						.equals(mergeObject.getMerge_status()))) {
			logger.info("----merge state is opened = true -----"
					+ mergeObject.getMerge_status());
			// 更新的操作还是不对，还需要处理
			try {
				boolean res = storageMerge(mergeObject, mergeRequest);
				if (res == true) {
					logger.info("---------------请求入库成功");
					applyMerge(mergeObject, mergeRequest);
				}

			} catch (Exception e) {
				logger.error("-----------------请求入库失败", e);
			}

		} else {
			logger.info("----merge state is opened = false -----" + "不满足处理条件");
			if ("merged".equals(mergeObject.getState())
					&& ("can_be_merged".equals(mergeObject.getMerge_status()))
					&& mergeObject.getTarget_branch().indexOf("release") >= 0) {
				logger.info("运维处理后更新数据库");
				// 更新数据库为已经被运维处理
				mergeRequest.setMergeid(mergeObject.getId());
				mergeRequest.setStatus(PlatData.PUBLIST_SUCCESS_BYYUNEWI);
				componentService.updateStatusForYunWei(mergeRequest);
				
			}
			else if("closed".equals(mergeObject.getState())
					&& ("can_be_merged".equals(mergeObject.getMerge_status()))
					&& mergeObject.getTarget_branch().indexOf("release") >= 0){
				logger.info("关闭release请求的话更新记录为8，关闭");
				mergeRequest.setMergeid(mergeObject.getId());
				mergeRequest.setStatus(PlatData.PUBLIST_CLOSE_BYYUNEWI);
				componentService.updateStatusForYunWei(mergeRequest);
				
			}
		}

		// update table
		
	}

	// 加个开关，在第一次验证时用

	/**
	 * 判断是否符合要求
	 * 
	 * @param mergeObejectEntity
	 * @return
	 */
	public boolean isapply(MergeObejectEntity mergeObejectEntity,
			MegerRequestPO mergeRequest) {
		boolean isOk = false;
		logger.info("---当前的分支：： -----" + mergeObejectEntity.getTarget_branch());
		// 只对消息头验证
		if (BranchType.isAutoMerge(mergeObejectEntity.getTarget_branch())) {
			if (mergeObejectEntity.getTitle().lastIndexOf("CQ") >= 0
					|| mergeObejectEntity.getTitle().lastIndexOf("#") >= 0
					|| mergeObejectEntity.getTitle().lastIndexOf("cq") >= 0) {
				// 前面已经判断，无需判断
				/*
				 * if ("opened".equals(mergeObejectEntity.getState()) &&
				 * "unchecked".equals(mergeObejectEntity .getMerge_status())) {
				 * logger.info("----merge state is opened = true -----"); isOk =
				 * true; } else { mergeRequest.setDescription("merge的status不对::"
				 * + mergeObejectEntity.getMerge_status());
				 * logger.info("----验证不通过= false ，原因不明（merge的status不对）-----"); }
				 */
				isOk = true;
			} else {
				logger.info("----title is not CQ&#&cq = false -----");
				mergeRequest.setDescription("merge的请求头 必须有bug号或者需求号::"
						+ (mergeObejectEntity.getTitle()));
				try {
					componentService.updateByMergeId(mergeRequest);
				} catch (Exception e1) {
					logger.error("更新失败", e1);
				}
			}
		} else {
			if (StringUtils.isNotEmpty(mergeObejectEntity.getDescription())) {
				mergeRequest.setDescription("人工审核::"
						+ (mergeObejectEntity.getDescription()));
			} else {
				mergeRequest.setDescription("人工审核::"
						+ (mergeRequest.getDescription()));
			}

			logger.info("----  targetbranch  is merge的分支是要人工审核的 =false-----");
			try {
				componentService.updateByMergeId(mergeRequest);
			} catch (Exception e1) {
				logger.error("更新失败", e1);
			}
		}

		return isOk;
	}

	/*
	 * public static void main(String[] args) { PlatformService service = new
	 * PlatformService(); MergeObejectEntity mergeObject = new
	 * MergeObejectEntity(); mergeObject.setTarget_project_id(312);
	 * mergeObject.setId("10494"); service.applyMerge(mergeObject); String aa
	 * ="See merge request"; System.out.println(aa.indexOf("merge request"));
	 * 
	 * }
	 */

	public GitlabHTTPRequestor retrieve(GitlabAPI api) {
		return new GitlabHTTPRequestor(api).authenticate(
				PlatData.PRIVATE_TOKEN, TokenType.PRIVATE_TOKEN,
				AuthMethod.HEADER);
	}

	private void applyMerge(MergeObejectEntity mergeObject,
			MegerRequestPO mergeRequest) {
		String path = "";
		if (!isapply(mergeObject, mergeRequest)) {
			logger.info("------------不符合规则，不进行处理----------------");
			return;
		}
		/**
		 * 参考http://gitlab.scm.cfets.com/help/api/merge_requests.md
		 */
		logger.info("=============符合规则的自动合并代码开始============");
		GitlabAPI api = null;
		// project,merge id ,iid
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
						componentService.updateByMergeId(mergeRequest);
					} catch (Exception e1) {
						logger.error("更新失败", e1);
					}
					return;
				}
			}
		}
		mapflagInit.put("flag", "accept  merge init");
		// 初始化文件
		path = "init-publish-version";
		// writeFile(mergeObject, mapflagInit, path);
		if (!BranchType.isPulish(mergeObject.getTarget_branch())) {
			logger.info("由于这个版本不发版，往后不执行：" + mergeObject.getTarget_branch());
			mergeRequest.setDescription("代码已经自动合并，但是这个版本不发版暂时不发版");
			try {
				componentService.updateByMergeId(mergeRequest);
			} catch (Exception e1) {
				logger.error("更新失败", e1);
			}
			return;
		}
		// 开始验证代码--下载最新代码，验证pom，打包
		logger.info("=============符合自动审核和发版的数据开始发版验证============");
		Map<String, Object> mapflag = new HashMap<String, Object>();
		cloneProiectHandler.setSuccessor(valitePomHandler);
		valitePomHandler.setSuccessor(mavenHandler);
		// 发布定时发版
		// mavenHandler.setSuccessor(deployProjectHandler);
		mapflag = cloneProiectHandler.handle(mergeObject, mapflag);
		logger.info("最后处理的结果：：：" + mapflag);
		if (mapflag.get("flag").equals(true)) {
			mergeRequest.setStatus(PlatData.MAVEAN_SUCCESS);
			// mergeRequest.setDescription(mapflag.toString());
			mergeRequest.setVersion(mapflag.get("newVersion").toString());
			try {
				componentService.updateByMergeId(mergeRequest);
			} catch (Exception e1) {
				logger.error("更新失败", e1);
			}
			// 通关
			// 验证通过之后自动创建merge
			// 204是小娜的号 144是我的号,这个数据是==getMemeber()
			/*
			 * String title = mergeObject.getTitle();
			 * logger.info("project id createMerge : target " +
			 * mergeObject.getTarget_branch());
			 * logger.info("project id createMerge  projectId :" +
			 * mergeObject.getTarget_project_id());
			 * logger.info("project title createMerge :" + title);
			 */
			/*
			 * try { GitlabMergeRequest newRequest = api.createMergeRequest(
			 * mergeObject.getTarget_project_id(),
			 * mergeObject.getTarget_branch(), targetNew, 204, title);
			 * mergeRequest.setRelationId(Long.valueOf(newRequest.getId())); try
			 * { componentService.updateByMergeId(mergeRequest); } catch
			 * (Exception e1) { logger.error("更新失败", e1); } } catch (IOException
			 * e) { logger.error("create merge fail", e); }
			 */
			// 将信息写入数据库,建立关系--未做
			path = "publish-version";
			// writeFile(mergeObject, mapflag, path);
		} else if ("false".equals(mapflag.get("flag"))) {
			mergeRequest.setStatus(PlatData.MAVEAN_FAIL);
			mergeRequest.setDescription(mapflag.toString());
			if (mapflag.get("newVersion") != null) {
				mergeRequest.setVersion(mapflag.get("newVersion").toString());
			}
			try {
				componentService.updateByMergeId(mergeRequest);
			} catch (Exception e1) {
				logger.error("更新失败", e1);
			}
			logger.info("失败：：：：" + mergeObject.getTarget_branch() + "：：："
					+ mapflag);
			path = "publish-version";
			// writeFile(mergeObject, mapflag, path);
		} else {
			// 未知结果
			mergeRequest.setStatus(PlatData.MAVEAN_DEFALUT);
			mergeRequest.setDescription(mapflag.toString());
			mergeRequest.setVersion(mapflag.get("newVersion").toString());
			try {
				componentService.updateByMergeId(mergeRequest);
			} catch (Exception e1) {
				logger.error("更新失败", e1);
			}
			logger.info("失败：：：：" + mergeObject.getTarget_branch() + "：：："
					+ mapflag);
			path = "publish-version";
			// writeFile(mergeObject, mapflag, path);

		}

	}

	/*
	 * private Map<String, Integer> parseDiff(String diff) { Map<String,
	 * Integer> ms = new HashMap<String, Integer>(); int addNum = 0; int delNum
	 * = 0; File file = new File("temp.txt"); PrintWriter pw = null;
	 * BufferedReader br = null; try { pw = new PrintWriter(file);
	 * pw.write(diff); pw.flush(); br = new BufferedReader(new
	 * FileReader(file)); String line = ""; int i = 0; while ((line =
	 * br.readLine()) != null) {
	 * 
	 * System.out.println(line); if (line.startsWith("@@")) { i++; continue; }
	 * if (i == 1) { if ('+' == line.toCharArray()[0]) { addNum++; } else if
	 * ('-' == line.toCharArray()[0]) { delNum++; } } } } catch (Exception e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } finally { try {
	 * pw.close(); } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } try { br.close(); } catch (IOException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } } ms.put("addNum",
	 * addNum); ms.put("delNum", delNum); boolean isDel = file.delete();
	 * logger.info("----------temp 文件是否删除成功------------- " + isDel); return ms;
	 * }
	 */

	private void writeFile(MergeObejectEntity mergeObject,
			Map<String, Object> mapflag, String path) {

		// 将发版信息复制出到文件夹
		logger.info("----------begin write file-------------- ");
		String uploadLogPath = path;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String datePath = sdf1.format(new Date());
		String filePath = uploadLogPath + "/" + uploadLogPath + "-" + datePath
				+ ".txt";
		// 创建文件夹
		createDir(uploadLogPath);
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("create file fail", e);
			}
		}
		FileWriter pw = null;

		String date = sf.format(new Date());
		try {
			pw = new FileWriter(filePath, true);
			pw.write(mergeObject.getTarget().getName() + "  "
					+ mergeObject.getTitle() + "\r\n");
			pw.write("详细信息：" + mapflag.toString() + "\r\n");
			pw.write("所属分支：" + mergeObject.getTarget_branch().toString()
					+ "\r\n");
			pw.write(new Date() + "\r\n");
			pw.flush();
		} catch (Exception e) {
			logger.error("pw.write fail", e);
		} finally {
			if (pw != null) {
				try {
					pw.close();
				} catch (IOException e) {
					logger.error("pw.close  fail", e);
				}
			}
		}
		logger.info("---------- write file end -------------- ");
	}

	private boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			System.out.println("创建目录失败，目标目录已存在！");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {// 创建目标目录
			System.out.println("创建目录成功！" + destDirName);
			return true;
		} else {
			System.out.println("创建目录失败！");
			return false;
		}
	}

	private synchronized boolean storageMerge(MergeObejectEntity mergeObject,
			MegerRequestPO mergeRequest) {
		// 有就更新,无就新增,更改的时候调用
		List<MegerRequestPO> list = componentService
				.getMergeRequestPOByMergeId(mergeObject.getId());
		if (list == null || list.size() == 0) {
			logger.info("---------- 开始新增初始化数据，新增数据的mergeID -------------- "
					+ mergeObject.getId());
			mergeRequest.setProjectName(mergeObject.getTarget().getName());
			mergeRequest.setAuthorName(recommond.getUser().getName());
			mergeRequest.setDescription(mergeObject.getDescription());
			mergeRequest.setRequestTime(DateUtils.utcToLocalDate(mergeObject
					.getCreated_at()));
			mergeRequest.setBranch(mergeObject.getTarget_branch());
			mergeRequest.setMergeid(mergeObject.getId());
			mergeRequest.setTitle(mergeObject.getTitle());
			mergeRequest.setStatus(PlatData.PUBLIST_DEFAULTL);
			// 默认值
			if (mergeObject.getTarget_branch().indexOf("release") >= 0) {
				mergeRequest.setStatus(PlatData.PUBLIST_SUCCESS);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				mergeRequest.setDescription(componentService
						.getDescriptionByMergeId(mergeObject.getId()));
				mergeRequest.setVersion(componentService
						.getVersionByMergeId(mergeObject.getId()));
				logger.info("---------- 开始新增初始化数据，新增数据的版本号-------------- "
						+ mergeRequest.getVersion());
				// 如果是手动发起的请求，要更改原来的状态为ID
			}
			mergeRequest.setProjectId(mergeObject.getTarget_project_id());
			mergeRequest.setIsVersion(BranchType.isPulish(mergeObject
					.getTarget_branch()) ? "0" : "1");

			  
			// 更新状态
			// 处理手动提交release的情况
			if (mergeObject.getTarget_branch().indexOf("release") >= 0) {
				logger.info("---------- 开始更新原来数据(手动执行会有效)-------------- ");
				componentService.updateMasterByname(mergeRequest);
			}
			return componentService.save(mergeRequest);
		} else {
			logger.info("---------- 开始更新mergeID -------------- "
					+ list.get(0).getMergeid());
			logger.info("---------- 开始更新描述 -------------- "
					+ mergeObject.getDescription());
			mergeRequest = list.get(0);
			mergeRequest.setDescription(mergeObject.getDescription());
			mergeRequest.setTitle(mergeObject.getTitle());
			return componentService.updateByMergeId(mergeRequest);

		}

	}

}
