package com.cfets.ts.u.platform.job;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.cwap.s.util.DateUtil;
import com.cfets.ts.u.platform.ExploringAxonApplication;
import com.cfets.ts.u.platform.bean.SumWorkOfMember;
import com.cfets.ts.u.platform.bean.User;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.service.ComponentService;
import com.cfets.ts.u.platform.util.DateUtils;
import com.cfets.ts.u.platform.web.DataSumOne;
import com.cfets.ts.u.platform.web.PlatformEventRest;
/*
 * 统计工作日报
 */
@Service
public class CopyOfCommitEventJob {
	@Autowired
	private ComponentService componentService;
	private static final Logger logger = Logger.getLogger(CopyOfCommitEventJob.class);
	private static DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	int sumNum=0;
	public void dayOfDelay() {
		long oneDay = 24 * 60 * 60 * 1000;
		 sumNum = componentService.sumNum();
		// 要延迟的时间，如果是比23：00小，则>0,取剩余的时间，否则就取+1小时的时间
		long initDay = geTimeMills("20:20:00")
				- System.currentTimeMillis();
		initDay = initDay > 0 ? initDay : oneDay + initDay;
		logger.info("统计总数是：" + sumNum);
		executor.scheduleAtFixedRate(new CommitEvent1(componentService,sumNum),
				initDay, oneDay, TimeUnit.MILLISECONDS);
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

	/*
	 * public static void main(String[] args) { CommitEventJob job = new
	 * CommitEve ntJob(); job.dayOfDelay();
	 * 
	 * }
	 */

}

class CommitEvent1 implements Runnable {
	private static final Logger logger = Logger.getLogger(CommitEvent.class);
	private ComponentService componentService;
	private int sumNum;

	public CommitEvent1(ComponentService componentService,int sumNum) {
		this.componentService = componentService;
		this.sumNum=sumNum;
	}

	@Override
	public void run() {
		try {
			File file = new File(PlatData.FILEPATH);
			File[] fl = file.listFiles();
			for (File one : fl) {
				File[] one1 = one.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						if (pathname.getName().endsWith(".git")) {
							return true;
						}
						return false;
					}
				});
				if (one1.length > 0) {
					jGitSum(one1[0], one.getName(),sumNum);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//判断数据变量===============
	private void jGitSum(File file, String fileName,int sumNum) {
		// 创建本地仓库
		// todo 如果是新工程就clone
		try {
			org.eclipse.jgit.lib.Repository repo = new FileRepositoryBuilder()
					.setGitDir(file).build();
			Git git = new Git(repo);
			// 获取所有的分支名字
			Map<String, Ref> map = git.getRepository().getAllRefs();
			Set<String> keys = map.keySet();
			logger.info("------------------------------start-----------------------------");
			logger.info("当前工程名：" + fileName);
			for (String one11 : keys) {
				// 目前写死--master，远程分支
				if (one11.indexOf("master") >= 0
						&& one11.indexOf("refs/remotes") >= 0) {
					String branch = one11.substring(one11.lastIndexOf("/") + 1,
							one11.length());
					logger.info("要切换代码分支版本：：" + branch);
					logger.info("当前分支：：" + git.getRepository().getBranch());
					if (!branch.equals(git.getRepository().getBranch())) {
						//logger.info("要check分支");
						try {
							git.branchDelete().setBranchNames(branch)
									.setForce(true).call();
						} catch (Exception e1) {
							logger.error("删除分支异常", e1);
						}
						try {
							CheckoutCommand checkoutCommand = git.checkout();
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
						PullCommand pull = git.pull();
						PullResult resout = pull.call();
						if (resout.isSuccessful()) {
							logger.info("开始日志统计--");
							//统计pom更新为最新的--更新数据库
							if(one11.indexOf("master_1.0.8") >= 0
									&& one11.indexOf("refs/remotes") >= 0){
								changePom(fileName);
							}
							sumDevolper(git, branch, fileName,sumNum);
						}
					} catch (Exception e) {
						logger.error("pull失败", e);
					}

				}
				else {
					logger.info("不是master相关分支");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changePom(String fileName) {
		
		
	}

	private void sumDevolper(Git git, String branch, String plugin,int sumNum) {
		try {
			// 判断是否有值，如果有值，就取当天的数据
			Date before = DateUtils.getBeforeDay1(new Date());
			Date nowDay = DateUtils.getNowDay1(new Date());
			Date before7 = DateUtils.getServenDay(new Date());
			logger.info("before:"+before);
			logger.info("nowDay:"+nowDay);
			List<RevCommit> commitList = new ArrayList<RevCommit>();
			if (sumNum > 0) {
				logger.info("已经初始化了，不用初始化");
				// 已经初始化了，需要根据时间来判断
				LogCommand lg = git.log();
				Iterable<RevCommit> list = lg.call();
				int i = 0;
				for (RevCommit revcommit : list) {
					logger.info("开始循环数据revcommit::"+revcommit);
					if ("".equals(revcommit.getFullMessage())
							|| revcommit.getFullMessage().indexOf(
									"See merge request") >= 0) {
						i++;
						continue;

					}
                   //大于昨天23：50，<今天23：50
					if (revcommit.getCommitterIdent().getWhen().after(before)
							&& revcommit.getCommitterIdent().getWhen()
									.before(nowDay)) {
						commitList.add(revcommit);
					}
					if (revcommit.getCommitterIdent().getWhen().before(before)) {
						//加1个多余的
						logger.info("此统计了多余的"+revcommit.getName());
						commitList.add(revcommit);
						break;
					}

				}

			} else {
				logger.info("没有初始化");
				LogCommand lg = git.log();
				Iterable<RevCommit> list = lg.call();
				int i = 0;
				for (RevCommit revcommit : list) {
					if ("".equals(revcommit.getFullMessage())
							|| revcommit.getFullMessage().indexOf(
									"See merge request") >= 0) {
						i++;
						continue;

					}
					// 如果数据库有值，就取前一天到第二天的数据
					commitList.add(revcommit);

				}
			}
			logger.info("初级统计量："+commitList.size());
			for (int j = 0; j < commitList.size() - 1; j++) {
				// 判断一下，是不是含有第
				RevCommit comm = commitList.get(j);
				AbstractTreeIterator newTree = prepareTreeParser(
						commitList.get(j), git.getRepository());
				AbstractTreeIterator oldTree = prepareTreeParser(
						commitList.get(j + 1), git.getRepository());
				List<DiffEntry> diff = git.diff().setOldTree(oldTree)
						.setNewTree(newTree).setShowNameAndStatusOnly(true)
						.call();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				DiffFormatter df = new DiffFormatter(out);
				// 设置比较器为忽略空白字符对比（Ignores all whitespace）
				df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
				df.setRepository(git.getRepository());
				// 每一个diffEntry都是第个文件版本之间的变动差异
				int addSize = 0;
				int subSize = 0;
				for (DiffEntry diffEntry : diff) {
					// 打印文件差异具体内容
					df.format(diffEntry);
					String diffText = out.toString("UTF-8");
					// System.out.println(diffText);
					// 获取文件差异位置，从而统计差异的行数，如增加行数，减少行数
					FileHeader fileHeader = df.toFileHeader(diffEntry);
					List<HunkHeader> hunks = (List<HunkHeader>) fileHeader
							.getHunks();
					for (HunkHeader hunkHeader : hunks) {
						EditList editList = hunkHeader.toEditList();
						for (Edit edit : editList) {
							subSize += edit.getEndA() - edit.getBeginA();
							addSize += edit.getEndB() - edit.getBeginB();
						}
					}
					out.reset();
				}
				logger.info("commId::" + comm.name());
				logger.info("user_name=" + comm.getCommitterIdent().getName());
				logger.info("addSize=" + addSize);
				logger.info("subSize=" + subSize);

			
				logger.info("开始保存入库");
				Integer id = 0;
				try {
					id = ExploringAxonApplication.map.get(comm
							.getCommitterIdent().getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info("id::" + id);
				// 如果ID为空，保存数据到用户表
				if (id == null) {
					//如果内存里没有应该从数据库查一遍
					User user = new User();
					// 用户名，用户别名
					user.setName(comm.getCommitterIdent().getName());
					user.setUsername(comm.getCommitterIdent().getName());
					id = componentService.save(user);
					ExploringAxonApplication.map.put(comm.getCommitterIdent()
							.getName(), id);
				}
				// 保存入库：
				SumWorkOfMember sumWorkOfMember = new SumWorkOfMember();
				sumWorkOfMember.setAddNum(addSize);
				sumWorkOfMember.setDelNum(subSize);
				sumWorkOfMember.setSubTime(comm.getCommitterIdent().getWhen());
				sumWorkOfMember.setSubDate(DateUtil.getDateIgnoreTime(comm
						.getCommitterIdent().getWhen()));
				sumWorkOfMember.setUserId(id);
				sumWorkOfMember.setCommitId(comm.name());
				sumWorkOfMember.setPlugin(plugin);
				sumWorkOfMember.setBranch(branch);
				SimpleDateFormat sf = new SimpleDateFormat("yyyy");
				SimpleDateFormat sf1 = new SimpleDateFormat("MM");
				sumWorkOfMember.setYear(sf.format(comm.getCommitterIdent()
						.getWhen()));
				sumWorkOfMember.setMonth(sf1.format(comm.getCommitterIdent()
						.getWhen()));

				componentService.save(sumWorkOfMember);
			}
			logger.info("------------------------------end-----------------------------");
		} catch (Exception e) {
			logger.error("保存异常", e);
		}

	}

	private AbstractTreeIterator prepareTreeParser(RevCommit commit,
			org.eclipse.jgit.lib.Repository repository) {
		try (RevWalk walk = new RevWalk(repository)) {
			RevTree tree = walk.parseTree(commit.getTree().getId());
			CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
			try (ObjectReader oldReader = repository.newObjectReader()) {
				oldTreeParser.reset(oldReader, tree.getId());
			}

			walk.dispose();

			return oldTreeParser;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

}
