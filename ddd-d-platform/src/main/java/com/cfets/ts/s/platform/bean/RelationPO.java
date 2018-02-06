/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.bean;

import com.cfets.cwap.s.util.annotation.Note;
import com.cfets.cwap.s.util.db.Table;
import com.cfets.cwap.s.util.db.TableColumn;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年3月6日
 * 
 * @history
 * 
 */
@Table(name = "TSBASE.MAVEN_RULE")
public class RelationPO implements java.io.Serializable {

	private static final long serialVersionUID = -4758315615303270671L;

	@Note(note = "主键")
	@TableColumn(name = "SR_NO_ID", auto = true, sequence = "TSBASE.SEQ_MAVEN_RULE")
	private Long id;
	
	@Note(note = "pomId")
	@TableColumn(name = "MAIN_BSNS_RLTN_ID")
	private Long mainId;

	@TableColumn(name = "SHR_RL_ID")
	private Long depencyId;

	@TableColumn(name = "THE_MKT_DPTH_ID")
	private int degree;

	@TableColumn(name = "RULE_TP")
	private String scope;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMainId() {
		return mainId;
	}

	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}

	public Long getDepencyId() {
		return depencyId;
	}

	public void setDepencyId(Long depencyId) {
		this.depencyId = depencyId;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}


}
