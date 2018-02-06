package com.cfets.ts.u.platform.web;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import com.cfets.cwap.s.util.DateUtil;
import com.cfets.ts.u.platform.ExploringAxonApplication;
import com.cfets.ts.u.platform.bean.SumWorkOfMember;
import com.cfets.ts.u.platform.bean.User;
import com.cfets.ts.u.platform.service.ComponentService;
/**
 * 统计代码提交的数量
 * @author zrp
 *
 */
public class DataSumOne implements Runnable {
	private ComponentService componentService;
	private static final Logger logger = Logger.getLogger(DataSumOne.class);
	private Git git;

	public DataSumOne(Git git, ComponentService componentService) {
		this.git = git;
		this.componentService = componentService;
	}

	@Override
	public void run() {
		logger.info("开始统计数据");
		try {
			LogCommand lg = git.log();
			List<RevCommit> commitList = new ArrayList<RevCommit>();
			Iterable<RevCommit> list = lg.call();
			int i = 0;
			for (RevCommit revcommit : list) {
				if (i == 2) {
					break;
				}
				if ("".equals(revcommit.getFullMessage())
						|| revcommit.getFullMessage().indexOf(
								"See merge request") >= 0) {
					i++;

				}
				if (i == 1 || i == 2) {
					commitList.add(revcommit);
				}

			}
			logger.info("开始统计数据大小"+commitList.size());
			for (int j=1; j < commitList.size() - 1; j++) {
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
				logger.info("------------------------------start-----------------------------");
				// 每一个diffEntry都是第个文件版本之间的变动差异
				int addSize = 0;
				int subSize = 0;
				for (DiffEntry diffEntry : diff) {
					// 打印文件差异具体内容
					df.format(diffEntry);
					String diffText = out.toString("UTF-8");
					System.out.println(diffText);
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
				logger.info("user_name="+comm.getCommitterIdent().getName());
				logger.info("addSize=" + addSize);
				logger.info("subSize=" + subSize);
				
				logger.info("------------------------------end-----------------------------");
				logger.info("开始保存入库");
				Integer id=0;
				try {
					 id =ExploringAxonApplication.map.get(comm.getCommitterIdent().getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info("id::"+id);
				//如果ID为空，保存数据到用户表
				if (id==null){
					User user = new User();
					//用户名，用户别名
					user.setName(comm.getCommitterIdent().getName());
					user.setUsername(comm.getCommitterIdent().getName());
					id =componentService.save(user);
				}
				// 保存入库：
				SumWorkOfMember sumWorkOfMember = new SumWorkOfMember();
				sumWorkOfMember.setAddNum(addSize);
				sumWorkOfMember.setDelNum(subSize);
				sumWorkOfMember.setSubTime(comm.getCommitterIdent().getWhen());
				sumWorkOfMember.setSubDate(DateUtil.getDateIgnoreTime(comm.getCommitterIdent().getWhen()));
				sumWorkOfMember.setUserId(id);
			/*	Calendar cal= Calendar.getInstance();
				cal.setTime(comm.getCommitterIdent().getWhen());
				Integer month =cal.getTime().getMonth();
				Integer year =cal.getTime().getYear();
				sumWorkOfMember.setMonth(String.valueOf(month)+1);
				sumWorkOfMember.setYear(String.valueOf(year));*/
				SimpleDateFormat sf = new SimpleDateFormat("yyyy");
				SimpleDateFormat sf1 = new SimpleDateFormat("MM");
				sumWorkOfMember.setYear(sf.format(comm.getCommitterIdent().getWhen()));
				sumWorkOfMember.setMonth(sf1.format(comm.getCommitterIdent().getWhen()));
				componentService.save(sumWorkOfMember);
			}
		} catch (Exception e) {
			logger.error("统计异常：：",e);
		}
	}
/*	public static void main(String[] args) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		SimpleDateFormat sf1 = new SimpleDateFormat("MM");
           System.out.println(sf.format(new Date()));
           System.out.println(sf1.format(new Date()));
           System.out.println(DateUtil.getDateIgnoreTime(new Date()));
	}*/

	public AbstractTreeIterator prepareTreeParser(RevCommit commit,
			org.eclipse.jgit.lib.Repository repository) {
		System.out.println(commit.getId());
		try (RevWalk walk = new RevWalk(repository)) {
			System.out.println(commit.getTree().getId());
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
