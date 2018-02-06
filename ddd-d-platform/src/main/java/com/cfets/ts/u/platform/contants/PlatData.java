package com.cfets.ts.u.platform.contants;


public class PlatData {
public static String FILEPATH="/home/ts/git";
public static String HTTPPATH="http://200.31.147.77";
public static String PRIVATE_TOKEN="4ZJPBCWdTNrJSRoQF6BT";
//统计开发人员时间
public static String SUMDEVLOGERDATA="23:30:00";
public static String CHANGEPOMFILE="23:10:00";
//数据库字段
public static String DEPLOYEETIMEONE="10:55:00";
public static String DEPLOYEETIMETWO="15:30:00";
//merge请求状态   0，发布失败;1,发布成功(发布到release)，2，未发布，4，打包成功，5，打包失败，6，未打包
public static String  PUBLIST_FAIL="0";
public static String  PUBLIST_SUCCESS="1";
public static String  PUBLIST_DEFAULTL="2";//初始状态
public static String  MAVEAN_FAIL="5";
public static String  MAVEAN_SUCCESS="4";
public static String  MAVEAN_DEFALUT="6";
public static String PUBLIST_SUCCESS_BYYUNEWI="7";//运维处理后的状态
public static String PUBLIST_CLOSE_BYYUNEWI="8";//运维关掉的状态
public static String  IS_PUBLIST="0";//要zidong发版
public static String  IS_NOT_PUBLISH="1";//不要zidong发版
}
