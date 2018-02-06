package com.cfets.ts.u.platform.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class DateUtils {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = Logger.getLogger(DateUtils.class);

	/**
	 * 昨天的23：30：00
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getBeforeDay(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_MONTH, -1);
		// 24小时
		calender.set(Calendar.HOUR_OF_DAY, 23);
		calender.set(Calendar.MINUTE, 30);
		calender.set(Calendar.SECOND, 00);
		return calender.getTime();
	}
	public static Date getBeforeDay1(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_MONTH, -11);
		// 24小时
		calender.set(Calendar.HOUR_OF_DAY, 23);
		calender.set(Calendar.MINUTE, 30);
		calender.set(Calendar.SECOND, 00);
		return calender.getTime();
	}
	public static Date getNowDay1(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_MONTH, -8);
		calender.set(Calendar.HOUR_OF_DAY, 23);
		calender.set(Calendar.MINUTE, 30);
		calender.set(Calendar.SECOND, 00);
		return calender.getTime();

	}
	/***
	 * 今天的23：30
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNowDay(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(Calendar.HOUR_OF_DAY, 23);
		calender.set(Calendar.MINUTE, 30);
		calender.set(Calendar.SECOND, 00);
		return calender.getTime();

	}

	/**
	 * 往前推七天 ,暂时不用
	 * 
	 * @param date
	 * @return
	 */
	public static Date getServenDay(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_MONTH, -7);
		return calender.getTime();

	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(getBeforeDay(new Date())); }
	 */
	public static Date utcToLocalDate(String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TimeZone utc = TimeZone.getTimeZone("UTC");
			df.setTimeZone(utc);
			Date dt = df.parse(date);
			return dt;
		} catch (Exception e) {
			logger.error("日期转换异常", e);
		}
		return new Date();
	}

}
