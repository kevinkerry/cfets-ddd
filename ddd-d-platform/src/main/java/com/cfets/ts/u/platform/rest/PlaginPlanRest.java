package com.cfets.ts.u.platform.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cfets.cwap.s.spi.GenericRest;
import com.cfets.cwap.s.stp.SimpleMessage;
import com.cfets.ts.u.platform.bean.MegerRequestVO;
import com.cfets.ts.u.platform.bean.MergeViewVO;
import com.cfets.ts.u.platform.service.ComponentService;

@RestController
@RequestMapping(PlatformHelper.PLUGIN_MAPPING)
public class PlaginPlanRest extends GenericRest {
	@Autowired
	private ComponentService componentService;

	@RequestMapping("/publishPlanForToday")
	public SimpleMessage<?> queryTodayPlaginPlan(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleMessage msg = SimpleMessage.blank();
		try {
			MergeViewVO vo = componentService
		 			.queryMergeViewVO(new Date());
			msg.set("mergeViewVO", vo);
		} catch (Exception e) {
			msg.error("数据查询异常");
		}
		return msg;

	}
	@RequestMapping("/publishPlanForHistory")
	public SimpleMessage<?> publishPlanForHistory(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleMessage msg = SimpleMessage.blank();
		try {
			List<Map<String,Object>>  vo = componentService
		 			.publishPlanForHistory(new Date());
			msg.set("mergeHistoryVO", vo);
		} catch (Exception e) {
			msg.error("数据查询异常");
			response.setStatus(509);
		}
		return msg;

	}
	/**
	 * 运维部署
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/planToDeploy")
	public SimpleMessage<?> planToDeply(HttpServletRequest request,
			HttpServletResponse response) {
		SimpleMessage msg = SimpleMessage.blank();
		try {
			List<MegerRequestVO>  vo = componentService
		 			.planToDeplyee(new Date());
			msg.set("planToDeployVO", vo);
		} catch (Exception e) {
			msg.error("数据查询异常");
			response.setStatus(509);
		}
		return msg;

	}
	
	
}
