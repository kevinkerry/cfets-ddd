package com.cfets.ts.u.platform.job;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jodd.madvoc.meta.Out;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabMergeRequest;
import org.gitlab.api.models.GitlabProject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.cwap.s.stp.SimpleMessage;
import com.cfets.ts.u.platform.bean.MegerRequestPO;
import com.cfets.ts.u.platform.bean.MegerRequestVO;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.jgit.DeployProjectHandlerTimer;
import com.cfets.ts.u.platform.service.ComponentService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

@Service
public class TimeDeployeeJobOne extends PublishJob {
	private static final Logger logger = Logger
			.getLogger(TimeDeployeeJobOne.class);
	/*
	 * private static DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	 * private static DateFormat dateFormat = new SimpleDateFormat(
	 * "yyyy-MM-dd HH:mm:ss");
	 */
	@Autowired
	DeployProjectHandlerTimer deployProjectHandlerTimer;
	@Autowired
	private ComponentService componentService;
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	@Override
	public void dayOfDelay() {
		long oneDay = 24 * 60 * 60 * 1000;
		// 要延迟的时间，如果是比23：00小，则>0,取剩余的时间，否则就取+1小时的时间
		long initDay = geTimeMills(PlatData.DEPLOYEETIMEONE)
				- System.currentTimeMillis();
		initDay = initDay > 0 ? initDay : oneDay + initDay;
		executor.scheduleAtFixedRate(new DeployeeEvent(), initDay, oneDay,
				TimeUnit.MILLISECONDS);
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
	class DeployeeEvent implements Runnable {
		@Override
		public void run() {
			logger.info("begin run 10:50");
			// 从昨天发版的数据到今天发版的数据--未发布和需要发版的数据
			List<MegerRequestVO> ls = componentService.queryMergeViewVO(
					PlatData.DEPLOYEETIMEONE, PlatData.DEPLOYEETIMEONE);
			if (ls != null && ls.size() > 0) {
				logger.info("10:50:00跑批的数量" + ls.size());
				Map<String, Object> map = new HashMap<String, Object>();
				deployProjectHandlerTimer.handle(ls, map);
				try {
					Thread.sleep(180000);
				} catch (InterruptedException e) {
					logger.info("休息失败");
				}
				// http://200.31.156.30:8034/ts/monitor/rest/ts-s-monitor/status?env=MON,ST2

				boolean isOk = getServiceStatus();
				if (isOk) {
					logger.info("-服务正常启动-----");
					// 发起merge请求
					GitlabAPI api = null;
					// project,merge id ,iid
					api = GitlabAPI.connect(PlatData.HTTPPATH,
							PlatData.PRIVATE_TOKEN);
					for (MegerRequestVO vo : ls) {
						logger.info("---------------------------begin fabangetVersion:"
								+ vo.getVersion());
						String targetNew = "";
						if ("ntp2master".equals(vo.getBranch())) {
							targetNew = "ntp2release";
							logger.info("------------ntp2master处理----------------");
						} else if ("master_1.0.8".equals(vo.getBranch())) {
							targetNew = "release_1.0.8";
							logger.info("------------master_1.0.8处理----------------");
						} else if ("master_1.0.9".equals(vo.getBranch())) {
							targetNew = "release_1.0.9";
							logger.info("------------master_1.0.9处理----------------");
						} else {
							logger.info("------------不是108|109|110不处理----------------");
							return;
						}
						// 从数据库查
						try {
							GitlabMergeRequest newRequest = api
									.createMergeRequest(vo.getProductId(),
											vo.getBranch(), targetNew, 204,
											vo.getTitle());
						/*	api.updateMergeRequest(vo.getProductId(),
									newRequest.getId(), targetNew, 204,
									vo.getTitle(), vo.getDescription(), "", "");*/
							vo.setRelationId(newRequest.getId() + "");
							MegerRequestPO po = new MegerRequestPO();
							BeanUtils.copyProperties(vo, po);
							logger.info("-------------定时任务开更新原来的数据----------");
							try {
								componentService.updateByMergeId(po);
							} catch (Exception e1) {
								logger.error("更新失败", e1);
							}
						} catch (Exception e) {
							logger.error("create merge fail", e);
							vo.setRelationId( "冲突版本");
							MegerRequestPO po = new MegerRequestPO();
							BeanUtils.copyProperties(vo, po);
							logger.info("-------------多人开发有可能会提冲突   ----------");
							try {
								componentService.updateByMergeId(po);
							} catch (Exception e1) {
								logger.error("更新失败", e1);
							}
						}
					}
					logger.info("-服务正常启动任务完成-----");
				} else {
					logger.info("-服务正常启动异常-----");
					for (MegerRequestVO vo : ls) {
						vo.setStatus(PlatData.PUBLIST_FAIL);
						MegerRequestPO po = new MegerRequestPO();
						BeanUtils.copyProperties(vo, po);
						try {
							componentService.update(po);
						} catch (Exception e1) {
							logger.error("更新失败", e1);
						}
					}
					logger.info("-服务正常启动异常工作完成-----");
				}

			}

		}

	}

}
