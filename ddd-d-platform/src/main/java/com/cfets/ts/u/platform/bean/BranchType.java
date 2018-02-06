package com.cfets.ts.u.platform.bean;

import java.util.ArrayList;
import java.util.List;

//是否自动merge 
/**
 * 
 *  分支类型，merge类型(1,是人工审核；0，自动审核，release都是1,master看情况),是否发版(1,不发版，0，发版)
 * @author zrp
 * 测试：109要发版
 */
public enum BranchType {
	//00,10
	// 分支类型，merge类型(1,是人工审核；0，自动审核，release都是1,master看情况),是否发版(1,不发版，0，发版)
	//如果是人工审核的话，就不用管发版的，不会走到打包的过程的,目前都存成1，不发版
	//如果是自动审核，要判断是否要版
	//release*都写死(1,1)什么操作都不需要，开发只要判断master即可
	master("master", 1,1), release("release", 1,1), master2("master2", 1,1), master105(
			"master_1.0.5", 1,1), release105("release_1.0.5", 1,1), master108(
			"master_1.0.8", 1,1), release108("release_1.0.8", 1,1), master109(
			"master_1.0.9", 1,1), release109("release_1.0.9", 1,1), ntp2master(
			"ntp2master", 0,0), ntp2release("ntp2release", 1,0);
	//分支类型
	private String type;
	private int autoType;
	private int isPublish;

	private BranchType(String type, int autoType,int isPublish) {
		this.type = type;
		this.autoType = autoType;
		this.isPublish=isPublish;
	}

	public String getType() {
		return type;
	}

	public int getAutoType() {
		return this.autoType;
	}

	public int getIsPublish() {
		return isPublish;
	}


	/**
	 * 获取所有自动merge项
	 * 
	 * @return
	 */
	public static List<String> getAllAuto() {
		List<String> ls = new ArrayList<String>();
		for (BranchType one : BranchType.values()) {
			if (one.getAutoType() == 0) {
				ls.add(one.getType().toString());
			}
		}
		return ls;
	}

	// 所有不要自动merge的操作
	public static List<String> getAllNotAuto() {
		List<String> ls = new ArrayList<String>();
		for (BranchType one : BranchType.values()) {
			if (one.getAutoType() == 1) {
				ls.add(one.getType().toString());
			}
		}
		return ls;
	}
   //判断是否自动合并
	public static boolean isAutoMerge(String branch) {
		for (String one : BranchType.getAllAuto()) {
			if (one.toString().equals(branch)) {
				return true;
			}
		}
		return false;
	}
	//判断是否发版
	public static boolean isPulish(String branch) {
		for (String one : BranchType.isAllpublish()) {
			if (one.toString().equals(branch)) {
				return true;
			}
		}
		return false;
	}
	
	public static List<String> isAllpublish() {
		List<String> ls = new ArrayList<String>();
		for (BranchType one : BranchType.values()) {
			if (one.getIsPublish() == 0) {
				ls.add(one.getType().toString());
			}
		}
		return ls;
	}
	
	
}
