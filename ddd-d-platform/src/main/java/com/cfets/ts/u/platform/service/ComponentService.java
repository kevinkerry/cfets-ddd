/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.u.platform.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cfets.ts.u.platform.bean.ComponentPO;
import com.cfets.ts.u.platform.bean.MegerRequestPO;
import com.cfets.ts.u.platform.bean.MegerRequestVO;
import com.cfets.ts.u.platform.bean.MergeHistoryVO;
import com.cfets.ts.u.platform.bean.MergeViewVO;
import com.cfets.ts.u.platform.bean.RelationPO;
import com.cfets.ts.u.platform.bean.SumWorkOfMember;
import com.cfets.ts.u.platform.bean.User;
import com.cfets.ts.u.platform.job.CacheVO;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年3月11日
 * 
 * @history
 * 
 */
public interface ComponentService {
	boolean save(ComponentPO po);
	public Long getId(String group, String artifactId, String version);
	boolean save(RelationPO po);
	public  List<RelationPO> getList(Long one, Long anther);
	public String getVersion(String group, String artifactId, String branchType);
	//List<ComponentVO> findAll();
	/*List<Map> findMavenDepency();
	List<SourceOrTarget> findRalations(Long id);*/
	void save(SumWorkOfMember sumWorkOfMember);
	int save(User user);
	int sumNum();
	public void save(List<User> users);
	boolean  save(MegerRequestPO mergeRequest);
	boolean update(MegerRequestPO mergeRequest);
	List<MegerRequestVO> queryTodayPlaginPlan(Date date);
	MergeViewVO queryMergeViewVO(Date date);
	List<MegerRequestVO> queryMergeViewVO(String endTime,
                                          String beginTime);
	List<Map<String,Object>> publishPlanForHistory(Date date);
	boolean updateByMergeId(MegerRequestPO mergeRequest);
	String getVersionByMergeId(String mergeId);
	List<MegerRequestPO> getMergeRequestPOByMergeId(String id);
	String getDescriptionByMergeId(String id);
	int[] batchUpdateVersion(List<CacheVO> list);
	boolean updateStatusForYunWei(MegerRequestPO mergeRequest);
	List<MegerRequestVO> planToDeplyee(Date date);
	boolean  updateMasterByname(MegerRequestPO mergeRequest);

}
