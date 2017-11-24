package com.cfets.ts.s.task.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cfets.ts.s.task.entity.po.RelationPO;
import com.cfets.ts.s.task.entity.po.SchedulePO;
import com.cfets.ts.s.task.entity.po.TaskPO;

/**
 * 任务调度
 * 
 * @author sikaiqi
 *         <p>
 *         2017-01-28 sikaiqi 创建任务调度service
 *         </p>
 *         <p>
 *         2017-06-01 sikaiqi merge代码:从ntp2master分支merge到release
 *         </p>
 *         <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 */
public class TaskService {

	private static List<SchedulePO> schedulePOList = new ArrayList<SchedulePO>();
	private static Map<Long, List<TaskPO>> taskMap = new HashMap<Long, List<TaskPO>>();
	private static Map<Long, List<RelationPO>> relationMap = new HashMap<Long, List<RelationPO>>();

	private static void init() {

		schedulePOList.add(new SchedulePO(1L, "闭市批处理任务NTPI-外汇期权"));// 0
		// start by sikaiqi 2017-06-01 for merge
		// schedulePOList.add(new SchedulePO(6L, "闭市批处理任务NTPI-外币拆借"));//1
		// schedulePOList.add(new SchedulePO(7L, "闭市批处理任务NTPI-货币掉期"));//2
		schedulePOList.add(new SchedulePO(2L, "闭市批处理任务NTPI-外币拆借"));// 1
		schedulePOList.add(new SchedulePO(3L, "闭市批处理任务NTPI-货币掉期"));// 2
		// end by sikaiqi 2017-06-01 for merge

		schedulePOList.add(new SchedulePO(4L, "闭市批处理任务NTPI-外币利率互换"));// 3
		schedulePOList.add(new SchedulePO(5L, "闭市批处理任务NTPI-无本金交割"));// 4
		schedulePOList.add(new SchedulePO(6L, "闭市批处理任务NTPII/OTRADE-贵金属市场批处理"));// 5
		schedulePOList.add(new SchedulePO(7L, "闭市批处理任务NTPII/OTRADE-即远掉批处理"));// 6
		schedulePOList.add(new SchedulePO(8L, "闭市批处理任务NTPII/OTRADE-报价行情批处理"));// 7

		List<TaskPO> fxoClosingMarketNTPIPOs = new ArrayList<TaskPO>();
		List<TaskPO> fxclClosingMarketNTPIPOs = new ArrayList<TaskPO>();
		List<TaskPO> ccsClosingMarketNTPIPOs = new ArrayList<TaskPO>();
		List<TaskPO> irsClosingMarketNTPIPOs = new ArrayList<TaskPO>();
		List<TaskPO> rndfClosingMarketNTPIPOs = new ArrayList<TaskPO>();
		List<TaskPO> goldClosingMarketNTPIIPOs = new ArrayList<TaskPO>();
		List<TaskPO> fxsptFxfFxwapClosingMarketNTPIIPOs = new ArrayList<TaskPO>();
		List<TaskPO> qtDataClosingMarketNTPIIPOs = new ArrayList<TaskPO>();

		/**
		 * 闭市任务
		 */
		// 外汇期权、对冲、exop 成交明细
		fxoClosingMarketNTPIPOs.add(new TaskPO(1L, schedulePOList.get(0)
				.getScheduleId(),
				"com.cfets.ts.u.fxodeal.task.FxoDataTransferTask",
				"外汇期权（期权，对冲，exop）成交明细数据结转任务"));
		// 外汇期权成交行情
		fxoClosingMarketNTPIPOs.add(new TaskPO(2L, schedulePOList.get(0)
				.getScheduleId(),
				"com.cfets.ts.s.dealmarket.task.FxoMarketDataTransferTask",
				"外汇期权成交行情数据结转任务"));
		// 外汇期权报价行情
		fxoClosingMarketNTPIPOs.add(new TaskPO(3L, schedulePOList.get(0)
				.getScheduleId(),
				"com.cfets.ts.u.fxoquote.task.ClearFxOptnDpthQtDataTask",
				"外汇期权报价行情数据清理任务"));
		// 外币拆借成交明细
		fxclClosingMarketNTPIPOs.add(new TaskPO(4L, schedulePOList.get(1)
				.getScheduleId(),
				"com.cfets.ts.u.fxcldeal.task.FxclDataTransferTask",
				"外币拆借成交明细数据结转任务"));
		// 外币拆借成交行情
		fxclClosingMarketNTPIPOs.add(new TaskPO(5L, schedulePOList.get(1)
				.getScheduleId(),
				"com.cfets.ts.s.dealmarket.task.FxclMarketDataTransferTask",
				"外币拆借成交行情数据结转任务"));
		// 外币拆借报价行情
		fxclClosingMarketNTPIPOs.add(new TaskPO(6L,schedulePOList.get(1)
				.getScheduleId(),
				"com.cfets.ts.u.fxclquote.task.ClearFrgnCcyLndngEachQtDataTask",
				"外币拆借报价行情数据清理任务"));
		// 货币掉期成交明细
		ccsClosingMarketNTPIPOs.add(new TaskPO(7L, schedulePOList.get(2)
				.getScheduleId(),
				"com.cfets.ts.u.crsdeal.task.FirdvDataTransferTask",
				"货币掉期成交明细数据结转任务"));
		// 货币掉期成交行情
		ccsClosingMarketNTPIPOs.add(new TaskPO(8L, schedulePOList.get(2)
				.getScheduleId(),
				"com.cfets.ts.s.dealmarket.task.CrsMarketDataTransferTask",
				"货币掉期成交行情数据结转任务"));
		// 货币掉期报价行情
		ccsClosingMarketNTPIPOs.add(new TaskPO(9L, schedulePOList.get(2)
				.getScheduleId(),
				"com.cfets.ts.u.crsquote.task.ClearCrsQtDataTask",
				"货币掉期报价行情数据清理任务"));

		// 外币利率互换成交明细 TODO
		irsClosingMarketNTPIPOs.add(new TaskPO(10L, schedulePOList.get(3)
				.getScheduleId(),
				"",
				"外币利率互换成交明细数据结转任务")); 
		// 外币利率互换成交行情 TODO
		irsClosingMarketNTPIPOs.add(new TaskPO(11L, schedulePOList.get(3)
				.getScheduleId(),
				"",
				"外币利率互换成交行情数据结转任务"));

		// 贵金属成交明细
		goldClosingMarketNTPIIPOs.add(new TaskPO(12L, schedulePOList.get(5)
				.getScheduleId(),
				"com.cfets.ts.s.dealcommon.task.GldMarketDataTransferTask",
				"贵金属市场成交明细数据结转任务"));
		// 贵金属成交行情
		goldClosingMarketNTPIIPOs.add(new TaskPO(13L, schedulePOList.get(5)
				.getScheduleId(),
				"com.cfets.ts.s.dealmarket.task.PrecMarketDataTransferTask",
				"贵金属市场成交行情数据结转任务"));

		// 即远掉成交明细
		fxsptFxfFxwapClosingMarketNTPIIPOs.add(new TaskPO(14L, schedulePOList
				.get(6).getScheduleId(),
				"com.cfets.ts.s.dealcommon.task.FxMarketDataTransferTask",
				"即远掉成交明细数据结转任务"));
		// 即远掉成交行情
		fxsptFxfFxwapClosingMarketNTPIIPOs.add(new TaskPO(15L, schedulePOList
				.get(6).getScheduleId(),
				"com.cfets.ts.s.dealmarket.task.FxsfsMarketDataTransferTask",
				"即远掉成交行情数据结转任务"));

		// 二期报价行情
		qtDataClosingMarketNTPIIPOs.add(new TaskPO(16L, schedulePOList.get(7)
				.getScheduleId(),
				"com.cfets.ts.s.quotecommon.task.ClearFxUsdVsHkdBestQtTask",
				"美元兑港元最优报价数据清理"));
		qtDataClosingMarketNTPIIPOs.add(new TaskPO(17L, schedulePOList.get(7)
				.getScheduleId(),
				"com.cfets.ts.s.quotecommon.task.ClearODMSpotForwardQtTask",
				"ODM即期远期报价行情数据清理"));
		qtDataClosingMarketNTPIIPOs.add(new TaskPO(18L, schedulePOList.get(7)
				.getScheduleId(),
				"com.cfets.ts.s.quotecommon.task.ClearODMSwapQtTask",
				"ODM掉期报价行情数据清理"));
		qtDataClosingMarketNTPIIPOs.add(new TaskPO(19L, schedulePOList.get(7)
				.getScheduleId(),
				"com.cfets.ts.s.quotecommon.task.ClearQDMSpotForwardQtTask",
				"QDM即期远期报价行情数据清理"));
		qtDataClosingMarketNTPIIPOs.add(new TaskPO(20L, schedulePOList.get(7)
				.getScheduleId(),
				"com.cfets.ts.s.quotecommon.task.ClearQDMSwapQtTask",
				"QDM掉期报价行情数据清理"));

		taskMap.put(schedulePOList.get(0).getScheduleId(),
				fxoClosingMarketNTPIPOs);
		taskMap.put(schedulePOList.get(1).getScheduleId(),
				fxclClosingMarketNTPIPOs);
		taskMap.put(schedulePOList.get(2).getScheduleId(),
				ccsClosingMarketNTPIPOs);

		/*taskMap.put(schedulePOList.get(3).getScheduleId(),
				irsClosingMarketNTPIPOs);*/
		taskMap.put(schedulePOList.get(5).getScheduleId(),
				goldClosingMarketNTPIIPOs);
		taskMap.put(schedulePOList.get(6).getScheduleId(),
				fxsptFxfFxwapClosingMarketNTPIIPOs);
		taskMap.put(schedulePOList.get(7).getScheduleId(),
				qtDataClosingMarketNTPIIPOs);

		List<RelationPO> fxoClosingMarketNTPIRelationPOs = new ArrayList<RelationPO>();
		List<RelationPO> fxclClosingMarketNTPIRelationPOs = new ArrayList<RelationPO>();
		List<RelationPO> ccsClosingMarketNTPIRelationPOs = new ArrayList<RelationPO>();
		List<RelationPO> irsClosingMarketNTPIRelationPOs = new ArrayList<RelationPO>();
		List<RelationPO> goldClosingMarketNTPIIRelationPOs = new ArrayList<RelationPO>();
		List<RelationPO> fxsptFxfFxwapClosingMarketNTPIIRelationPOs = new ArrayList<RelationPO>();
		List<RelationPO> qtDataClosingMarketNTPIIRelationPOs = new ArrayList<RelationPO>();

		/**
		 * 闭市任务关系
		 */
		fxoClosingMarketNTPIRelationPOs.add(new RelationPO(1L, 1L, 1L, 1));
		fxoClosingMarketNTPIRelationPOs.add(new RelationPO(2L, 1L, 2L, 2));
		fxoClosingMarketNTPIRelationPOs.add(new RelationPO(3L, 1L, 3L, 3));
		// start by sikaiqi 2017-06-01 for merge
		// fxclClosingMarketNTPIRelationPOs.add(new RelationPO(4L, 6L, 4L, 1));
		// fxclClosingMarketNTPIRelationPOs.add(new RelationPO(5L, 6L, 5L, 2));
		// fxclClosingMarketNTPIRelationPOs.add(new RelationPO(6L, 6L, 6L, 3));
		// firdvClosingMarketNTPIRelationPOs.add(new RelationPO(7L, 7L, 7L, 1));
		// firdvClosingMarketNTPIRelationPOs.add(new RelationPO(8L, 7L, 8L, 2));
		// firdvClosingMarketNTPIRelationPOs.add(new RelationPO(9L, 7L, 9L, 3));
		fxclClosingMarketNTPIRelationPOs.add(new RelationPO(4L, 2L, 4L, 1));
		fxclClosingMarketNTPIRelationPOs.add(new RelationPO(5L, 2L, 5L, 2));
		fxclClosingMarketNTPIRelationPOs.add(new RelationPO(6L, 2L, 6L, 3));
		ccsClosingMarketNTPIRelationPOs.add(new RelationPO(7L, 3L, 7L, 1));
		ccsClosingMarketNTPIRelationPOs.add(new RelationPO(8L, 3L, 8L, 2));
		ccsClosingMarketNTPIRelationPOs.add(new RelationPO(9L, 3L, 9L, 3));
		// end by sikaiqi 2017-06-01 for merge

		irsClosingMarketNTPIRelationPOs.add(new RelationPO(10L, 4L, 10L, 1));
		irsClosingMarketNTPIRelationPOs.add(new RelationPO(11L, 4L, 11L, 2));
		goldClosingMarketNTPIIRelationPOs.add(new RelationPO(12L, 6L, 12L, 1));
		goldClosingMarketNTPIIRelationPOs.add(new RelationPO(13L, 6L, 13L, 2));
		fxsptFxfFxwapClosingMarketNTPIIRelationPOs.add(new RelationPO(14L, 7L,
				14L, 1));
		fxsptFxfFxwapClosingMarketNTPIIRelationPOs.add(new RelationPO(15L, 7L,
				15L, 2));
		qtDataClosingMarketNTPIIRelationPOs.add(new RelationPO(16L, 8L, 16L, 1));
		qtDataClosingMarketNTPIIRelationPOs.add(new RelationPO(17L, 8L, 17L, 2));
		qtDataClosingMarketNTPIIRelationPOs.add(new RelationPO(18L, 8L, 18L, 3));
		qtDataClosingMarketNTPIIRelationPOs.add(new RelationPO(19L, 8L, 19L, 4));
		qtDataClosingMarketNTPIIRelationPOs.add(new RelationPO(20L, 8L, 20L, 5));

		// relation关系
		relationMap.put(schedulePOList.get(0).getScheduleId(),
				fxoClosingMarketNTPIRelationPOs);
		relationMap.put(schedulePOList.get(1).getScheduleId(),
				fxclClosingMarketNTPIRelationPOs);
		relationMap.put(schedulePOList.get(2).getScheduleId(),
				ccsClosingMarketNTPIRelationPOs);
		relationMap.put(schedulePOList.get(3).getScheduleId(),
				irsClosingMarketNTPIRelationPOs);
		relationMap.put(schedulePOList.get(5).getScheduleId(),
				goldClosingMarketNTPIIRelationPOs);
		relationMap.put(schedulePOList.get(6).getScheduleId(),
				fxsptFxfFxwapClosingMarketNTPIIRelationPOs);
		relationMap.put(schedulePOList.get(7).getScheduleId(),
				qtDataClosingMarketNTPIIRelationPOs);

	}

	static {
		init();
	}

	public List<TaskPO> getTaskListByScheduleId(Long scheduleId) {
		List<TaskPO> taskPOs = taskMap.get(scheduleId);
		List<RelationPO> relationPOs = relationMap.get(scheduleId);
		Collections.sort(relationPOs, new Comparator<RelationPO>() {

			@Override
			public int compare(RelationPO o1, RelationPO o2) {

				return o1.getTaskStep().compareTo(o2.getTaskStep());
			}
		});
		List<TaskPO> combineTaskPos = new ArrayList<TaskPO>();
		for (RelationPO item : relationPOs) {
			for (TaskPO item2 : taskPOs) {
				if (item.getTaskId() == item2.getTaskId()) {
					combineTaskPos.add(item2);
				}
			}
		}
		return combineTaskPos;
	}
}
