package com.cfets.ts.u.platform.job;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabMergeRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.ts.u.platform.bean.MegerRequestPO;
import com.cfets.ts.u.platform.bean.MegerRequestVO;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.jgit.DeployProjectHandlerTimer;
import com.cfets.ts.u.platform.service.ComponentService;
/**
 * ceshi demo
 * @author zrp
 *
 */
public class DemoEvent  extends PublishJob implements Runnable{
	private static final Logger logger = Logger
			.getLogger(DemoEvent.class);
	@Autowired
	private ComponentService componentService;
	@Autowired
	DeployProjectHandlerTimer deployProjectHandlerTimer;
	@Override
	public void run() {

		logger.info("begin run 10:50");
		// 从昨天发版的数据到今天发版的数据--未发布和需要发版的数据
		List<MegerRequestVO> ls = componentService.queryMergeViewVO(
				PlatData.DEPLOYEETIMEONE, PlatData.DEPLOYEETIMETWO);
		if (ls != null && ls.size() > 0) {
			logger.info("10:50:00跑批的数量" + ls.size());
			Map<String, Object> map = new HashMap<String, Object>();
			//deployProjectHandlerTimer.handle(ls, map);
			/*try {
				Thread.sleep(180000);
			} catch (InterruptedException e) {
				logger.info("休息失败");
			}*/
			// http://200.31.156.30:8034/ts/monitor/rest/ts-s-monitor/status?env=MON,ST2

			boolean isOk = true;
			//boolean isOk = getServiceStatus();
			if (isOk) {
				// 发起merge请求

				GitlabAPI api = null;
				// project,merge id ,iid
				api = GitlabAPI.connect(PlatData.HTTPPATH,
						PlatData.PRIVATE_TOKEN);
				for (MegerRequestVO vo : ls) {
					logger.info("---------------------------begin fabangetVersion:" +vo.getVersion());
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
					//从数据库查
					try {
						GitlabMergeRequest newRequest = api
								.createMergeRequest(vo.getProductId(),
										vo.getBranch(),
										targetNew, 204, vo.getTitle());
						vo.setRelationId(newRequest.getId()+"");
						MegerRequestPO po = new MegerRequestPO();
						BeanUtils.copyProperties(vo, po);
						try {
							componentService.updateByMergeId(po);
						} catch (Exception e1) {
							logger.error("更新失败", e1);
						}
					} catch (IOException e) {
						logger.error("create merge fail", e);
					}
				}
			} else {
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
			}

		}

	

	}

	@Override
	public void dayOfDelay() {
		// TODO Auto-generated method stub
		
	}

}
