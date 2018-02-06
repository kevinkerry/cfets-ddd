package com.cfets.ts.u.platform.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MergeHistoryVO implements Serializable {
	private static final long serialVersionUID = -1647127455220008592L;
	private String publishDate;
	private  List<MegerRequestVO> mergeList = new  ArrayList<MegerRequestVO>();
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public List<MegerRequestVO> getMergeList() {
		return mergeList;
	}
	public void setMergeList(List<MegerRequestVO> mergeList) {
		this.mergeList = mergeList;
	}

}
