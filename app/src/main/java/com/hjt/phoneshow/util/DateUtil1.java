package com.hjt.phoneshow.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil1 {

	private static String mYear;
	private static String mMonth;
	private static String mDay;
	private static String mHour;
	private static String mMinute;
	private static String mSecond;
	
	private static String mWay;
	private static Calendar c;

	/**
	 * 返回时间的年月日的字符串
	 * @return  2016-12-12
	 */
	public static String StringData() {
		c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		return mYear + "-" + mMonth + "-" + mDay;
	}
	/**
	 * 获取当前时间的时分秒
	 * @return  08-16-25
	 */
	public static String getDate() {
		mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));// 获取当天的时间
		mMinute = String.valueOf(c.get(Calendar.MINUTE));// 获取分钟
		mSecond = String.valueOf(c.get(Calendar.SECOND));// 获取秒数
		if(mMinute.length()==1){
			return " "+mHour + ":0" + mMinute + ":" + mSecond;
		}else if(mSecond.length()==1){
			return " "+mHour + ":" + mMinute + ":0" + mSecond;
		}else{
			return " "+mHour + ":" + mMinute + ":" + mSecond;
		}
	}

	/**
	 * 返回时间的星期几的字符串
	 * @return  星期三
	 */
	public static String StringWeek() {
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "天";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return "星期" + mWay;
	}
	
	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/**
	 * 获取当天日期 yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	
	/**
	 * 为拍完照照片命名 yyyy-MM-dd HH:mm:ss.png
	 * @return
	 */
	public static String getCurrentTimeForImage(){
        return getCurrentTime() + ".png";
	}
}
