/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.service;

import java.util.List;
import java.util.Map;

import com.cfets.ts.s.platform.bean.ComponentPO;
import com.cfets.ts.s.platform.bean.ComponentVO;
import com.cfets.ts.s.platform.bean.RelationPO;
import com.cfets.ts.s.platform.bean.RueryPO;
import com.cfets.ts.s.platform.bean.SourceOrTarget;

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
	List<ComponentVO> findAll();
	List<Map> findMavenDepency();
	List<SourceOrTarget> findRalations(Long id);
	
	
	List<ComponentPO> findall();
	List<RueryPO> findComponent(String filename);
	List<RueryPO> findComponenttwo(String cname);
}
