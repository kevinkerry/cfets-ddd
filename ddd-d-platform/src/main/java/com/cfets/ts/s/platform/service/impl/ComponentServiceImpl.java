/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;

import com.cfets.cwap.s.util.db.filter.FilterAttrs;
import com.cfets.ts.s.platform.PlatformHelper;
import com.cfets.ts.s.platform.bean.ComponentPO;
import com.cfets.ts.s.platform.bean.ComponentVO;
import com.cfets.ts.s.platform.bean.RelationPO;
import com.cfets.ts.s.platform.bean.RueryPO;
import com.cfets.ts.s.platform.bean.SourceOrTarget;
import com.cfets.ts.s.platform.dao.PlatformDao;
import com.cfets.ts.s.platform.dao.PlatformDaoImpl;
import com.cfets.ts.s.platform.service.ComponentService;


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
public class ComponentServiceImpl implements ComponentService {

	@Override
	public boolean save(ComponentPO po) {
		// 如果有就更新，如果没有就插入
		Long id = getId(po.getGroupId(), po.getArtifactId(), po.getVersion());
		if (id != null) {
			po.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			FilterAttrs attrs = FilterAttrs.blank();
			attrs.add("SR_NO_ID", id);
			int num = PlatformHelper.getJdbcManager().update(po, attrs);
			if (num > 0) {
				return true;
			}
		} else {
			String sql = "insert into tsbase.MAVEN_DATA (SR_NO_ID, GRP_NM, FILE_NM, WDGT_VRSN, MTDT_DESC, CRT_TM, ISR_EN_SHRT_NM, EN_CNTNT_LINK_ADRS, UPD_TM, CRTR, UPDTR, USR_NM) values (TSBASE.SEQ_MAVEN_DATA.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			List<Object> list = new ArrayList<Object>();
			list.add(po.getGroupId());
			list.add(po.getArtifactId());
			list.add(po.getVersion());
			list.add(po.getDescription());
			list.add(new Date());
			list.add(po.getDeveloper());
			list.add(po.getUrl());
			list.add(new Date());
			list.add(po.getMakeUser());
			list.add(po.getUpdateUser());
			list.add(po.getName());
			int num = PlatformHelper.getJdbcManager().getJt()
					.update(sql, list.toArray());
			if (num > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Long getId(String group, String artifactId, String version) {
		FilterAttrs attrs = FilterAttrs.blank();
		attrs.add("GRP_NM", group);
		attrs.add("FILE_NM", artifactId);
		if (StringUtils.isNotEmpty(version)) {
			attrs.add("WDGT_VRSN", version);
		}
		List<ComponentPO> po = PlatformHelper.getJdbcManager().find(
				ComponentPO.class, attrs);
		if (po != null && po.size() > 0) {
			return po.get(0).getId();
		}
		return null;

	}

	@Override
	public boolean save(RelationPO po) {
		List<RelationPO> lr = getList(po.getMainId(), po.getDepencyId());
		if (lr != null && lr.size() > 0) {
			FilterAttrs attrs = FilterAttrs.blank();
			attrs.add("SR_NO_ID", lr.get(0).getId());
			int num = PlatformHelper.getJdbcManager().update(po, attrs);
			if (num > 0) {
				return true;
			}
		} else {
			return PlatformHelper.getJdbcManager().save(po);
		}
		return false;
	}

	@Override
	public List<RelationPO> getList(Long one, Long anther) {
		FilterAttrs attrs = FilterAttrs.blank();
		attrs.add("MAIN_BSNS_RLTN_ID", one);
		attrs.add("SHR_RL_ID", anther);
		List<RelationPO> po = PlatformHelper.getJdbcManager().find(
				RelationPO.class, attrs);
		return po;
	}

	@Override
	public List<ComponentVO> findAll() {
		List<ComponentPO> po = PlatformHelper.getJdbcManager().find(
				ComponentPO.class);
		List<ComponentVO> ls = new ArrayList<ComponentVO>();
		for (ComponentPO cp : po) {
			ComponentVO vo = new ComponentVO();
			BeanUtils.copyProperties(cp, vo);
			ls.add(vo);
		}
		return ls;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> findMavenDepency() {
		final List<Map> ls = new ArrayList<Map>();
		String sql = "SELECT  SR_NO_ID, FILE_NM ,WDGT_VRSN FROM TSBASE.MAVEN_DATA";
		PlatformHelper.getJdbcManager().getJt()
				.query(sql, new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						 Map<String,Object> map = new HashMap<String,Object>();
						String fileName = rs.getString("FILE_NM");
						map.put("name", fileName+"["+rs.getString("WDGT_VRSN")+"]");
						map.put("id", rs.getLong("SR_NO_ID"));
						ls.add(map);
						return fileName;
					}

				});
		
		return ls;
	}

	@Override
	public List<SourceOrTarget> findRalations(Long id) {
		final List<SourceOrTarget> ls = new ArrayList<SourceOrTarget>();
		String sql = "SELECT   MAIN_BSNS_RLTN_ID,SHR_RL_ID  FROM TSBASE.MAVEN_RULE WHERE MAIN_BSNS_RLTN_ID= "+id;
		PlatformHelper.getJdbcManager().getJt()
				.query(sql, new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						long source = rs.getLong("MAIN_BSNS_RLTN_ID");
						long target=rs.getLong("SHR_RL_ID");
						SourceOrTarget st = new SourceOrTarget();
						st.setSource(source+"");
						st.setTarget(target+"");
						ls.add(st);
						return "";
					}

				});
		return ls;
	
	}
	private PlatformDao platformDao = new PlatformDaoImpl();
	@Override
	public List<ComponentPO> findall() {
		// TODO Auto-generated method stub
		return platformDao.findAll();
	}  
    @Override
    public  List<RueryPO> findComponenttwo(String filename){
    	String sql="select b.sr_no_id,b.file_nm,a.audt_tp,b.wdgt_vrsn,tsbase.maven_rule.rule_tp from tsbase.maven_data a,tsbase.maven_rule,tsbase.maven_data b where a.sr_no_id=tsbase.maven_rule.main_bsns_rltn_id  and tsbase.maven_rule.shr_rl_id = b.sr_no_id  and a.file_nm ='"+filename+"'";
    	List<RueryPO> listthree=PlatformHelper.getJdbcManager().getJt().query(sql,new RowMapper<RueryPO>(){
    		@Override
    		public RueryPO mapRow(ResultSet rs,int rowNum) throws SQLException{
    			RueryPO r=new RueryPO();
    			r.setAudt_tp(rs.getString("audt_tp"));
    			r.setRule_tp(rs.getString("rule_tp"));
                r.setId(rs.getLong("SR_NO_ID"));
                r.setArtifactId(rs.getString("FILE_NM"));
                r.setVersion(rs.getString("WDGT_VRSN"));
				return r;
    		}
    	});
    	return listthree;
    }
	@Override
	public List<RueryPO> findComponent(String filename) {		
		String sql="select b.sr_no_id,b.file_nm,a.audt_tp,b.wdgt_vrsn,tsbase.maven_rule.rule_tp from tsbase.maven_data a,tsbase.maven_rule,tsbase.maven_data b where a.sr_no_id=tsbase.maven_rule.shr_rl_id  and tsbase.maven_rule.main_bsns_rltn_id = b.sr_no_id  and a.file_nm ='"+filename+"'";
		List<RueryPO> listtwo=PlatformHelper.getJdbcManager().getJt().query(sql, new RowMapper<RueryPO>() {			
			@Override
			public RueryPO mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				RueryPO c=new RueryPO();
				c.setAudt_tp(rs.getString("audt_tp"));
    			c.setRule_tp(rs.getString("rule_tp"));
                c.setId(rs.getLong("SR_NO_ID"));
                c.setArtifactId(rs.getString("FILE_NM"));
                c.setVersion(rs.getString("WDGT_VRSN"));
				return c;
			}
		});
		return listtwo;
	}

}
