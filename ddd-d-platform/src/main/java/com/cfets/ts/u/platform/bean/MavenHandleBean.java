package com.cfets.ts.u.platform.bean;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class MavenHandleBean {

	private Object obj;
	private Date date;
	private int num;
	public MavenHandleBean(Object obj)
	{
		this.obj = obj;
		setNum(3);
		setDate(new Date());
	}
	public boolean allowExcute()
	{
		Date newDate = DateUtils.addMinutes(getDate(), 30);
		Date date = new Date();
		if(newDate.after(date) && getNum()>1)
		{
			return true;
		}else
		{
			return false;
		}
	}
	public void excute()
	{
		setNum(getNum()-1);
	}
	
	public Object getObj() {
		return obj;
	}
	public boolean isCompleted()
	{
		return getNum() <= 1;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

	
}
