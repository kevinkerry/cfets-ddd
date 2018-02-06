package com.cfets.ts.s.platform.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import javax.swing.tree.TreePath;

import org.springframework.jdbc.core.RowMapper;

import com.cfets.ts.s.platform.PlatformHelper;
import com.cfets.ts.s.platform.bean.ComponentPO;
import com.cfets.ts.s.platform.PlatformHelper;



public class PlatformDaoImpl implements PlatformDao {

	
	@Override
	public List<ComponentPO> findAll() {
		// TODO Auto-generated method stub
		String sql="select * from TSBASE.MAVEN_DATA";
		List<ComponentPO> list=PlatformHelper.getJdbcManager().getJt().query(sql,new RowMapper<ComponentPO>(){
         
			public ComponentPO mapRow(ResultSet rs, int arg1) throws SQLException {
			
			ComponentPO po =new ComponentPO();
			po.setId((long) rs.getInt("SR_NO_ID"));
			po.setName(rs.getString("USR_NM"));
		    po.setUpdateUser(rs.getString("UPDTR"));
		    po.setDescription(rs.getString("MTDT_DESC"));
		    po.setUpdateTime(rs.getTimestamp("UPD_TM"));
		    po.setVersion(rs.getString("WDGT_VRSN")); 
		    po.setUrl(rs.getString("EN_CNTNT_LINK_ADRS"));
		    po.setGroupId(rs.getString("GRP_NM"));
		    po.setArtifactId(rs.getString("FILE_NM"));
		    po.setCreateTime(rs.getTimestamp("CRT_TM"));
		    
		    po.setAudt(rs.getString("AUDT_TP"));
		    
		    po.setIscheck(rs.getString("ISCHECK"));
			return po;
			}
		}
		);
		return list;
	}

}
