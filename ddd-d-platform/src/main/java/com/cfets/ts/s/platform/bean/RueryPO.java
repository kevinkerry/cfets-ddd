/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.bean;

public class RueryPO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String audt_tp;
	private String rule_tp;
	private Long id;
	private String artifactId;
	private String version;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	public String getAudt_tp() {
		return audt_tp;
	}
	public void setAudt_tp(String audt_tp) {
		this.audt_tp = audt_tp;
	}
	public String getRule_tp() {
		return rule_tp;
	}
	public void setRule_tp(String rule_tp) {
		this.rule_tp = rule_tp;
	}
	
	
	

}
