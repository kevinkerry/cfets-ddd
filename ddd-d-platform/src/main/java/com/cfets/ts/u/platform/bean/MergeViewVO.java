package com.cfets.ts.u.platform.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 页面展示vo
 * 
 */
public class MergeViewVO implements Serializable {
	private static final long serialVersionUID = 5273138549005564789L;
	private String publishDate;
	private int num;
	private  List<MegerRequestVO> mergeList = new  ArrayList<MegerRequestVO>();

	// private String branch ;
	// private List<MegerRequestVO> lists;

	public int getNum() {
		return num;
	}

	

	public String getPublishDate() {
		return publishDate;
	}



	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}



	public void setNum(int num) {
		this.num = num;
	}



	public List<MegerRequestVO> getMergeList() {
		return mergeList;
	}



	public void setMergeList(List<MegerRequestVO> mergeList) {
		this.mergeList = mergeList;
	}

	

}
