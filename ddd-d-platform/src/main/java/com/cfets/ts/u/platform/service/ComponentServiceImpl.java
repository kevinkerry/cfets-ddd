package com.cfets.ts.u.platform.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.cfets.cwap.s.util.db.filter.FilterAttrs;
import com.cfets.ts.u.platform.ExploringAxonApplication;
import com.cfets.ts.u.platform.bean.ComponentPO;
import com.cfets.ts.u.platform.bean.MegerRequestPO;
import com.cfets.ts.u.platform.bean.MegerRequestVO;
import com.cfets.ts.u.platform.bean.MergeViewVO;
import com.cfets.ts.u.platform.bean.RelationPO;
import com.cfets.ts.u.platform.bean.SumWorkOfMember;
import com.cfets.ts.u.platform.bean.User;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.job.CacheVO;
import com.cfets.ts.u.platform.util.Mycompare;

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
@Component
public class ComponentServiceImpl implements ComponentService {
	private static final Logger logger = Logger
			.getLogger(ComponentServiceImpl.class);
	// public static DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean save(ComponentPO po) {
		// 如果有就更新，如果没有就插入
		Long id = getId(po.getGroupId(), po.getArtifactId(), po.getPomType());
		if (id != 0L) {
			int num = jdbcTemplate
					.update("update tsbase.MAVEN_DATA set USR_NM=?,MTDT_DESC=? where SR_NO_ID = ?",
							 po.getName(), po.getDescription(),
							id);
			if (num > 0) {
				return true;
			}
		} else {
			String sql = "insert into tsbase.MAVEN_DATA (SR_NO_ID, GRP_NM, FILE_NM, WDGT_VRSN, MTDT_DESC, CRT_TM, ISR_EN_SHRT_NM, EN_CNTNT_LINK_ADRS, UPD_TM, CRTR, UPDTR, USR_NM,AUDT_TP) values (TSBASE.SEQ_MAVEN_DATA.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
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
			list.add(po.getPomType());
			int num = jdbcTemplate.update(sql, list.toArray());
			logger.info("插入数量+==" + num);
			if (num > 0) {

				return true;
			}
		}
		return false;
	}

	// 不能根据版本号查，因为版本号不是最新的
	/**
	 * gr
	 */
	@Override
	public Long getId(String group, String artifactId, String branchType) {
		Long id = 0L;
		try {
			String sql = "SELECT  SR_NO_ID FROM TSBASE.MAVEN_DATA WHERE GRP_NM=? and FILE_NM=? and  AUDT_TP =?";
			/*
			 * id = jdbcTemplate.queryForObject(sql, new Object[] { group,
			 * artifactId, branchType }, Long.class);
			 */
			List<Long> ls = jdbcTemplate.query(sql, new RowMapper<Long>() {

				@Override
				public Long mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					Long id = rs.getLong("SR_NO_ID");
					return id;
				}
			}, new Object[] { group, artifactId, branchType });
			if (ls != null && ls.size() > 0) {
				return ls.get(0);
			}
		} catch (Exception e) {
			logger.error("查询有问题", e);
		}
		return (long) 0;

	}

	@Override
	public boolean save(RelationPO po) {
		List<RelationPO> lr = getList(po.getMainId(), po.getDepencyId());
		if (lr != null && lr.size() > 0) {
			String sql = "UPDATE  TSBASE.MAVEN_RULE  SET RULE_TP=? ,WDGT_VRSN = ? WHERE SR_NO_ID=?";
			int num = jdbcTemplate.update(sql,
					new Object[] { po.getScope(), po.getVersion(),
							lr.get(0).getId() });
			if (num > 0) {
				return true;
			}
		} else {
			String sql = "INSERT INTO  TSBASE.MAVEN_RULE(SR_NO_ID,MAIN_BSNS_RLTN_ID,SHR_RL_ID,THE_MKT_DPTH_ID,RULE_TP,WDGT_VRSN) VALUES(TSBASE.SEQ_MAVEN_RULE.NEXTVAL,?,?,?,?,?) ";
			int num = jdbcTemplate.update(sql, new Object[] { po.getMainId(),
					po.getDepencyId(), 1, po.getScope(), po.getVersion() });
			if (num > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<RelationPO> getList(Long one, Long anther) {
		List<RelationPO> list = new ArrayList<RelationPO>();
		String sql = "SELECT SR_NO_ID FROM TSBASE.MAVEN_RULE WHERE MAIN_BSNS_RLTN_ID=? and SHR_RL_ID=? ";
		list = jdbcTemplate.query(sql, new RowMapper<RelationPO>() {

			@Override
			public RelationPO mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				RelationPO po = new RelationPO();
				po.setId(rs.getLong("SR_NO_ID"));
				return po;
			}
		}, new Object[] { one, anther });
		return list;
	}

	@Override
	public void save(SumWorkOfMember sumWorkOfMember) {
		String sql = "INSERT INTO TSDEV.DEV_STAT(SR_NO_ID,USR_ID,ADD_NO,DEL_NO,SBMT_TM,SBMT_DT,SBMT_MTH,SBMT_YR,COMMIT_ID,PLUGIN,BRANCH) VALUES(SEQ_DEV_STAT.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
		int num = jdbcTemplate.update(
				sql,
				new Object[] { sumWorkOfMember.getUserId(),
						sumWorkOfMember.getAddNum(),
						sumWorkOfMember.getDelNum(),
						sumWorkOfMember.getSubTime(),
						sumWorkOfMember.getSubDate(),
						sumWorkOfMember.getMonth(), sumWorkOfMember.getYear(),
						sumWorkOfMember.getCommitId(),
						sumWorkOfMember.getPlugin(),
						sumWorkOfMember.getBranch() });
		if (num > 0) {
			logger.info("数据插入成功：：" + num);
		}
	}

	@Override
	public void save(List<User> ls) {
		// 做更新
		String query = "SELECT USR_NM FROM TSBASE.DEV_USR";
		List<String> dataHave = jdbcTemplate.queryForList(query, String.class);
		Map<String, String> ms = new HashMap<String, String>();
		for (String one : dataHave) {
			ms.put(one, one);
		}
		Iterator<User> iter = ls.iterator();
		while (iter.hasNext()) {
			User usr = iter.next();
			String one1 = usr.getUsername();
			if (ms.containsKey(one1)) {
				iter.remove();
			}
		}
		final List<User> ll = (ArrayList) ls;
		logger.info("新增的数据：：" + ls == null ? 0 : ls.size());
		String sql="";
		if(ll!=null&&ll.size()>0){
		String sql1 = "SELECT (MAX(USR_ID)+1) AS MNUM  FROM tsbase.dev_usr";
		final Integer id = jdbcTemplate.queryForObject(sql1,
				new RowMapper<Integer>() {

					@Override
					public Integer mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						// TODO Auto-generated method stub
						return rs.getInt("MNUM");
					}
				});
		// 剩余的就是新增用户
		 sql = "INSERT INTO TSBASE.DEV_USR(USR_ID,USR_NM ,USR_OTH_NM ) VALUES (?,?,?)";
		int[] cun = null;
		try {
			cun = jdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							ps.setLong(1, id);
							ps.setString(2, ll.get(i).getUsername());
							ps.setString(3, ll.get(i).getName());
						}

						@Override
						public int getBatchSize() {
							return ll.size();
						}
					});
		} catch (Exception e) {
			logger.error("插入更新异常", e);
		}
		}
		// 查询最新的数据
		sql = "SELECT  USR_NM,USR_ID AS UNM FROM TSBASE.DEV_USR ";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
		while (rs.next()) {
			ExploringAxonApplication.map.put(rs.getString(1), rs.getInt(2));
		}
		logger.info("map大小：：" + ExploringAxonApplication.map);
	}

	@Override
	public int save(User user) {
		String sql = "SELECT (MAX(USR_ID)+1) AS MNUM  FROM tsbase.dev_usr";
		Integer id = jdbcTemplate.queryForObject(sql, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getInt("MNUM");
			}
		});
		sql = "INSERT INTO TSBASE.DEV_USR(USR_ID,USR_NM,USR_OTH_NM ) VALUES(?,?,?)";
		int num = jdbcTemplate.update(sql,
				new Object[] { id, user.getUsername(), user.getName() });
		if (num > 0) {
			logger.info("数据插入成功：：" + num);
		}
		return id;
	}

	@Override
	public int sumNum() {
		String sql = "SELECT COUNT(*) AS sumNum  FROM  tsdev.dev_stat";
		Integer sumNum = jdbcTemplate.queryForObject(sql,
				new RowMapper<Integer>() {

					@Override
					public Integer mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						// TODO Auto-generated method stub
						return rs.getInt("sumNum");
					}
				});
		return sumNum;
	}

	@Override
	public String getVersion(String group, String artifactId, String branchType) {
		// TODO Auto-generated method stub
		try {
			String sql = "SELECT  WDGT_VRSN FROM TSBASE.MAVEN_DATA WHERE GRP_NM=? and FILE_NM=? and AUDT_TP=?";
			String version = jdbcTemplate.queryForObject(sql, new Object[] {
					group, artifactId, branchType }, String.class);
			return version;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.info("-------------数据库中无此构件----------");
			return null;
		}

	}

	@Override
	public boolean save(MegerRequestPO mergeRequest) {
		// 判断原来的值是不是有releation
		logger.info("-------------开始保存数据----------");
		String sql = "INSERT INTO  TSBASE.MERGE_REQUEST(sr_no_id,merge_id,authorname,merge_time,"
				+ "describe,project_name,BRANCH,title,STATUS,ISVERSION,PRODUCT_ID,VERSION) VALUES(SEQ_MERGE_REQUEST.nextval,?,?,?,?,?,?,?,?,?,?,?)";
		int num = jdbcTemplate
				.update(sql,
						new Object[] { mergeRequest.getMergeid(),
								mergeRequest.getAuthorName(),
								mergeRequest.getRequestTime(),
								mergeRequest.getDescription(),
								mergeRequest.getProjectName(),
								mergeRequest.getBranch(),
								mergeRequest.getTitle(),
								mergeRequest.getStatus(),
								mergeRequest.getIsVersion(),
								mergeRequest.getProjectId(),
								mergeRequest.getVersion() });
		if (num > 0) {
			logger.info("-------------更新成功----------");
			return true;
		}

		return false;
	}

	@Override
	public boolean update(MegerRequestPO mergeRequest) {
		logger.info("-------------更新成功----------更新的信息是："
				+ mergeRequest.getDescription() + "::SR_NO_ID::"
				+ mergeRequest.getId());
		String sql = "UPDATE TSBASE.MERGE_REQUEST SET STATUS=?, DESCRIBE=?,version=? WHERE SR_NO_ID=?";
		int num = jdbcTemplate.update(
				sql,
				new Object[] { mergeRequest.getStatus(),
						mergeRequest.getDescription(),
						mergeRequest.getVersion(), mergeRequest.getId() });
		if (num > 0) {
			logger.info("-------------更新成功----------");
			return true;
		}

		return false;
	}

	@Override
	public boolean updateByMergeId(MegerRequestPO mergeRequest) {
		String sql = "UPDATE TSBASE.MERGE_REQUEST SET STATUS=?, DESCRIBE=?,version=?, RELATION =?,TITLE=? WHERE MERGE_ID=?";
		int num = jdbcTemplate.update(
				sql,
				new Object[] { mergeRequest.getStatus(),
						mergeRequest.getDescription(),
						mergeRequest.getVersion(),
						mergeRequest.getRelationId(), mergeRequest.getTitle(),
						mergeRequest.getMergeid() });
		if (num > 0) {
			logger.info("-------------更新成功,就结束----------");
			return true;
		}

		return false;
	}

	@Override
	public List<MegerRequestVO> queryTodayPlaginPlan(Date date) {
		List<MegerRequestVO> list = new ArrayList<MegerRequestVO>();
		Calendar calent = Calendar.getInstance();
		calent.setTime(date);
		calent.set(Calendar.HOUR_OF_DAY, 0);
		calent.set(Calendar.MINUTE, 0);
		calent.set(Calendar.SECOND, 0);
		date = calent.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sdate = df.format(date);
		String sql = "SELECT * FROM tsbase.merge_request WHERE to_char(merge_time,'YYYY-MM-DD')=?  ORDER BY merge_time DESC ";
		list = jdbcTemplate.query(sql, new Object[] { sdate },
				new RowMapper<MegerRequestVO>() {

					@Override
					public MegerRequestVO mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						MegerRequestVO vo = new MegerRequestVO();
						vo.setId(rs.getLong("SR_NO_ID"));
						vo.setProjectName(rs.getString("project_name"));
						vo.setRequestTime(dateFormat.format(new Date(rs
								.getTimestamp("merge_time").getTime())));
						if (PlatData.PUBLIST_SUCCESS_BYYUNEWI.equals(rs
								.getString("status"))) {
							vo.setStatus("SUCCEED");
						} else if (PlatData.PUBLIST_FAIL.equals(rs
								.getString("status"))) {
							vo.setStatus("FAILURE");
						} else {
							vo.setStatus("DEFAULT");
						}

						vo.setBranch(rs.getString("branch"));
						vo.setAuthorName(rs.getString("authorname"));
						vo.setDescription(rs.getString("describe"));
						vo.setVersion(rs.getString("VERSION"));
						return vo;
					}
				});
		return list;
	}

	@Override
	public MergeViewVO queryMergeViewVO(Date date) {
		MergeViewVO vo = new MergeViewVO();
		List<MegerRequestVO> lm = queryTodayPlaginPlan(date);
		vo.setPublishDate(dayFormat.format(date));
		vo.setNum(lm.size());
		/*
		 * for (MegerRequestVO one : lm) { if
		 * (vo.getMergeList().containsKey(one.getBranch())) {
		 * vo.getMergeList().get(one.getBranch()).add(one); } else {
		 * List<MegerRequestVO> ls = new ArrayList<MegerRequestVO>();
		 * ls.add(one); vo.getMergeList().put(one.getBranch(), ls); }
		 * 
		 * }
		 */vo.setMergeList(lm);

		return vo;
	}

	// 打包成功未发布2-打包成功4-发布成功1
	// 0,是要发版，4打包成功
	// 发版成功
	@Override
	public List<MegerRequestVO> queryMergeViewVO(String endTime,
			String beginTime) {
		List<MegerRequestVO> list = new ArrayList<MegerRequestVO>();
		String sql = "SELECT * FROM tsbase.merge_request WHERE ISVERSION=? and STATUS=?  and branch like '%master%' and RELATION IS NULL";
		list = jdbcTemplate.query(sql, new Object[] { 0, 4 },
				new RowMapper<MegerRequestVO>() {
					@Override
					public MegerRequestVO mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						MegerRequestVO vo = new MegerRequestVO();
						vo.setId(rs.getLong("SR_NO_ID"));
						vo.setProjectName(rs.getString("project_name"));
						vo.setRequestTime(dateFormat.format(new Date(rs
								.getTimestamp("merge_time").getTime())));
						vo.setStatus(rs.getString("status"));
						vo.setBranch(rs.getString("branch"));
						vo.setAuthorName(rs.getString("authorname"));
						vo.setDescription(rs.getString("describe"));
						vo.setVersion(rs.getString("version"));
						vo.setProductId(rs.getInt("PRODUCT_ID"));
						vo.setTitle(rs.getString("TITLE"));
						vo.setIsVersion(rs.getString("ISVERSION"));
						vo.setMergeid(rs.getString("MERGE_ID"));
						return vo;
					}
				});
		return list;
	}

	@Override
	public List<Map<String, Object>> publishPlanForHistory(Date date) {
		List<Map<String, Object>> lss = new ArrayList<Map<String, Object>>();
		Map<String, List<MegerRequestVO>> map = new HashMap<String, List<MegerRequestVO>>();
		String sql = "SELECT * FROM tsbase.merge_request  order by merge_time desc ";
		List<MegerRequestVO> list = jdbcTemplate.query(sql,
				new RowMapper<MegerRequestVO>() {
					@Override
					public MegerRequestVO mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						MegerRequestVO vo = new MegerRequestVO();
						vo.setId(rs.getLong("SR_NO_ID"));
						vo.setProjectName(rs.getString("project_name"));
						vo.setRequestTime(dayFormat.format(new Date(rs
								.getTimestamp("merge_time").getTime())));
						if (PlatData.PUBLIST_SUCCESS_BYYUNEWI.equals(rs
								.getString("status"))) {
							vo.setStatus("SUCCESSED");
						} else if (PlatData.PUBLIST_FAIL.equals(rs
								.getString("status"))) {
							vo.setStatus("FAILURE");
						} else {
							vo.setStatus("DEFAULT");
						}

						vo.setBranch(rs.getString("branch"));
						vo.setAuthorName(rs.getString("authorname"));
						vo.setDescription(rs.getString("describe"));
						vo.setVersion(rs.getString("VERSION"));
						vo.setRequestTimeT(rs.getTimestamp("merge_time"));
						return vo;
					}
				});
		for (MegerRequestVO one : list) {
			if (map.size() == 0 || map.get(one.getRequestTime()) == null) {
				List<MegerRequestVO> ls = new ArrayList<MegerRequestVO>();
				ls.add(one);
				map.put(one.getRequestTime(), ls);
			} else {
				map.get(one.getRequestTime()).add(one);
			}
		}
		for (Map.Entry<String, List<MegerRequestVO>> entyt : map.entrySet()) {
			Map<String, Object> ll = new HashMap<String, Object>();
			ll.put("publishList", entyt.getValue());
			ll.put("publishDate", entyt.getKey());
			lss.add(ll);
		}
		Mycompare mm = new Mycompare();
		Collections.sort(lss, mm);

		return lss;
	}

	@Override
	public String getVersionByMergeId(String mergeId) {

		String sql = "SELECT VERSION FROM tsbase.merge_request  WHERE RELATION=? ";
		List<String> ls = jdbcTemplate.query(sql, new Object[] { mergeId },
				new RowMapper<String>() {

					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String version = rs.getString("VERSION");
						return version;
					}
				});
		if (ls != null && ls.size() > 0) {
			return ls.get(0);
		}
		return null;

	}

	@Override
	public List<MegerRequestPO> getMergeRequestPOByMergeId(String id) {
		List<MegerRequestPO> lm = new ArrayList<MegerRequestPO>();
		String sql = "SELECT * FROM tsbase.merge_request  WHERE MERGE_ID=? ";
		lm = jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<MegerRequestPO>() {

					@Override
					public MegerRequestPO mapRow(ResultSet rs, int rowNum)
							throws SQLException {

						MegerRequestPO vo = new MegerRequestPO();
						vo.setId(rs.getLong("SR_NO_ID"));
						vo.setProjectName(rs.getString("project_name"));
						vo.setRequestTime(rs.getTime("merge_time"));
						vo.setStatus(rs.getString("status"));
						vo.setBranch(rs.getString("branch"));
						vo.setAuthorName(rs.getString("authorname"));
						vo.setDescription(rs.getString("describe"));
						vo.setVersion(rs.getString("version"));
						vo.setProjectId(rs.getInt("PRODUCT_ID"));
						vo.setTitle(rs.getString("TITLE"));
						vo.setIsVersion(rs.getString("ISVERSION"));
						vo.setMergeid(rs.getString("MERGE_ID"));
						return vo;
					}
				});

		return lm;
	}

	@Override
	public String getDescriptionByMergeId(String mergeId) {

		String sql = "SELECT describe FROM tsbase.merge_request  WHERE RELATION=? ";
		List<String> ls = jdbcTemplate.query(sql, new Object[] { mergeId },
				new RowMapper<String>() {

					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String version = rs.getString("describe");
						return version;
					}
				});
		if (ls != null && ls.size() > 0) {
			return ls.get(0);
		}
		return null;

	}

	@Override
	public int[] batchUpdateVersion(final List<CacheVO> list) {
		String sql = "update  tsbase.maven_data set WDGT_VRSN=? where GRP_NM =? and FILE_NM=? and AUDT_TP=?  ";
		int[] cnt = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setString(1, list.get(i).getVersion());
						ps.setString(2, list.get(i).getGroupId());
						ps.setString(3, list.get(i).getArtifactId());
						ps.setString(4, list.get(i).getBranch());
					}

					@Override
					public int getBatchSize() {
						return list.size();
					}
				});
		return cnt;
	}

	@Override
	public boolean updateStatusForYunWei(MegerRequestPO mergeRequest) {

		String sql = "UPDATE TSBASE.MERGE_REQUEST SET STATUS=? WHERE MERGE_ID=?";
		int num = jdbcTemplate.update(
				sql,
				new Object[] { mergeRequest.getStatus(),
						mergeRequest.getMergeid() });
		if (num > 0) {
			logger.info("-------------更新成功updateStatusForYunWei----------");
			return true;
		}

		return false;

	}

	@Override
	public List<MegerRequestVO> planToDeplyee(Date date) {
		List<MegerRequestVO> list = new ArrayList<MegerRequestVO>();
		String sql = "SELECT * FROM tsbase.merge_request WHERE status=?  order by merge_time desc";
		list = jdbcTemplate.query(sql,
				new Object[] { PlatData.PUBLIST_SUCCESS },
				new RowMapper<MegerRequestVO>() {
					@Override
					public MegerRequestVO mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						MegerRequestVO vo = new MegerRequestVO();
						vo.setId(rs.getLong("SR_NO_ID"));
						vo.setProjectName(rs.getString("project_name"));
						vo.setRequestTime(dateFormat.format(new Date(rs
								.getTimestamp("merge_time").getTime())));
						vo.setStatus("DEFAULT");
						vo.setBranch(rs.getString("branch"));
						vo.setAuthorName(rs.getString("authorname"));
						vo.setDescription(rs.getString("describe"));
						// 这个要查询的
						vo.setVersion(rs.getString("VERSION"));
						return vo;
					}
				});
		return list;
	}
	//根据工程名工程ID,修改

	@Override
	public boolean updateMasterByname(MegerRequestPO mergeRequest) {
		boolean isOk = false;
		String updateSql = "update TSBASE.MERGE_REQUEST set RELATION=? where PRODUCT_ID=? AND BRANCH like '%master%' AND  RELATION IS NULL AND STATUS=4";
		int count = jdbcTemplate
				.update(updateSql, new Object[] { mergeRequest.getMergeid(),
						mergeRequest.getProjectId() });
		if (count > 0) {
			logger.info("更新数据量(手动合并的)："+count);
			isOk = true;
		} else {
			logger.info("没有要更新的记录："+count);
			isOk = false;
		}
		return isOk;

	}
}
