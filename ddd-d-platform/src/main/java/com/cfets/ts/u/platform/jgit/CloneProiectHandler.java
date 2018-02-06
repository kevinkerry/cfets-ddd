package com.cfets.ts.u.platform.jgit;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
import com.cfets.ts.u.platform.service.ComponentService;
import com.cfets.ts.u.platform.web.DataSumOne;
import com.cfets.ts.u.platform.web.PlatformEventRest;

/**
 * pull最新代码
 * 
 * @author zrp
 * 
 */
@Service("cloneProiectHandler")
public class CloneProiectHandler extends CodeHandler {
	
	private static final Logger logger = Logger
			.getLogger(CloneProiectHandler.class);
	@Autowired
	private ComponentService componentService;
	@Override
	public Map<String, Object> handle(MergeObejectEntity object,
			Map<String, Object> flag) {
		if (getSuccessor() != null) {
			logger.info("第一个handler（CloneProiectHandler）=========");
			// 如何更新代码 、分支的最新代码 这个是值clone当前的代码分支
			try {
				String filePath = PlatData.FILEPATH; // 克隆文件的位置
				// String filePath = "C://Users//zrp//git";
				String ntp2masterBranch = object.getTarget_branch();// 获取merge的目标分支
				File refile = new File(filePath + File.separator
						+ object.getTarget().getName() + File.separator
						+ ".git");// 当前文件
				// 创建本地仓库
				// todo 如果是新工程就clone
				org.eclipse.jgit.lib.Repository repo = new FileRepositoryBuilder()
						.setGitDir(refile).build();
				Git git = new Git(repo);
				/*
				 * git.reset().setMode(ResetType.HARD).setRef("HEAD") .call();
				 */
				// 获取所有的 本地分支列表
				ListBranchCommand ls = git.branchList();
				// 判断是否要重新创建分支，如果已经有的分支，不用再重新创建
				boolean isCheckout = true;
				File file = new File(filePath + File.separator
						+ object.getTarget().getName());
				// clone
				try {
					Git.cloneRepository()
							.setURI(object.getTarget().getWeb_url())
							.setDirectory(file).call();
				} catch (Exception e) {
					logger.error("代码clone失败,仓库有这个工程==不应该操作", e);
				}
				for (Ref ref : ls.call()) {
					// 本地分支名称
					if (ntp2masterBranch.equals(ref.getName().replaceFirst(
							"refs/heads/", ""))) {
						try {
							git.branchDelete().setBranchNames(ntp2masterBranch)
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
					logger.info("第一个handler（CloneProiectHandler===切换分支）");
					// 开始新建本地分支
					CheckoutCommand checkoutCommand = git.checkout();
					// 设置分支名称
					checkoutCommand.setName(ntp2masterBranch);
					// 分支是从那个remote取的代码，默认是master
					checkoutCommand.setStartPoint("refs/remotes/origin/"
							+ ntp2masterBranch);
					checkoutCommand.setCreateBranch(true);
					checkoutCommand.call();
				}
				// 拉取最新代码 异步的过程
				logger.info("第一个handler（CloneProiectHandler===pull开始）异步线程");
				PullCommand pull = git.pull();
				PullResult resout = pull.call();
				if (resout.isSuccessful()) {
					logger.info("第一个handler（CloneProiectHandler===pull结束）");
					flag.put("flag", true);
					flag.put("msg", "代码更新到本地成功");
					//进行统计数据开启异步线程：
					//PlatformEventRest.executor.execute(new DataSumOne(git,componentService));
					flag = getSuccessor().handle(object, flag);
				}

			} catch (Exception e) {
				logger.error("代码更新到本地失败", e);
				flag.put("flag", false);
				flag.put("msg", "代码更新到本地失败");
				return flag;// 异常的话就不要执行了
			}

		}
		return flag;

	}
  
/*	public static void main(String[] args) {
		CloneProiectHandler cloneProject = new CloneProiectHandler();
		Map<String, Object> flag = new HashMap<String, Object>();
		MergeObejectEntity object = new MergeObejectEntity();
		object.setTarget_branch("ntp2master");
		Target tar = new Target();
		tar.setWeb_url("git@gitlab.scm.cfets.com:TS/ts-s-deal.git");
		tar.setName("ts-s-deal");
		object.setTarget(tar);
		cloneProject.setSuccessor(cloneProject);
		Map<String, Object> m=cloneProject.handle(object, flag);
		System.out.println(m);
	}*/

}
