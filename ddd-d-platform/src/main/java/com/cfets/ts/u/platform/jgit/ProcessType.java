package com.cfets.ts.u.platform.jgit;
/**
 * 所有进程的枚举值
 * @author zrp
 *
 */
public enum ProcessType {    
	ams("ts-ams"), dqs("ts-dqs"), jss("ts-jss"), mc("ts-mc"), dps("ts-dps"), cssfx(
			"ts-css-fx"), css("ts-css");
	private String type;

	private ProcessType(String type) {
		this.type = type;
	}
	public String getValue(){
		return type;
	}
}
