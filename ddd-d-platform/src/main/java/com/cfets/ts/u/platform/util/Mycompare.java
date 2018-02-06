package com.cfets.ts.u.platform.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Map;

public class Mycompare implements Comparator<Map<String,Object>> {
	
	@Override
	public int compare(Map<String,Object> o1, Map<String,Object> o2) {
		 DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	String date1=	(String)o1.get("publishDate");
	String date2=	(String)o2.get("publishDate");
	try {
		if(dayFormat.parse(date1).getTime()< dayFormat.parse(date2).getTime()){
			return 1;
		}
		else if (dayFormat.parse(date1).getTime()> dayFormat.parse(date2).getTime()){
			return -1;
		}
		else {
			return 0;
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
		
	}

}
