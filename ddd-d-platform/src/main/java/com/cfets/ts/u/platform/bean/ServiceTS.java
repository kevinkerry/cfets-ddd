/**
 * 辅助交易系统ts-s-monitor
 */
package com.cfets.ts.u.platform.bean;

/**
 * 服务
 * 
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 */
public class ServiceTS implements java.io.Serializable {

	private static final long serialVersionUID = 8609447750136386134L;

	private long id;

	private String env;

	private String ip;

	private String port;

	private String context;

	private String name;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("env:" + env + " ");
		sb.append("name:" + name + " ");
		sb.append("ip:" + ip + " ");
		sb.append("port:" + port + " ");
		sb.append("context:" + context + " ");
		sb.append("status:" + status + " ");
		return sb.toString().trim();
	}



}
