/* vim: set expandtab sw=4 ts=4 sts=4: */
/**
 * @author:          eddie
 * @last modified:   2012-07-07 10:45:27
 * @filename:        MyJsonUtils.java
 * @description:     
 */
package com.ui.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.MyApplication.KsApplication;
import com.ui.ks.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		return toDate(sdate, dateFormater.get());
	}

	public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getDateString(Date date) {
		return dateFormater.get().format(date);
	}

	/**
	 * 以友好的方式显示时间
	 *多少时间之前
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(Context context,String sdate) {
		Date time = null;

		if (TimeZoneUtil.isInEasternEightZones())
			time = toDate(sdate);
		else
			time = TimeZoneUtil.transformTime(toDate(sdate),
					TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ context.getResources().getString(R.string.str295);//分钟前
			else
				ftime = hour + context.getResources().getString(R.string.str296);//小时前
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ context.getResources().getString(R.string.str295);//分钟前
			else
				ftime = hour + context.getResources().getString(R.string.str296);//小时前
		} else if (days == 1) {
			ftime = context.getResources().getString(R.string.str297);//昨天
		} else if (days == 2) {
			ftime = context.getResources().getString(R.string.str298);//前天
		} else if (days > 2 && days < 31) {
			ftime = days + context.getResources().getString(R.string.str299);//天前
		} else if (days >= 31 && days <= 2 * 31) {
			ftime = context.getResources().getString(R.string.str300);//一个月前
		} else if (days > 2 * 31 && days <= 3 * 31) {
			ftime = context.getResources().getString(R.string.str301);//2个月前
		} else if (days > 3 * 31 && days <= 4 * 31) {
			ftime = context.getResources().getString(R.string.str302);//3个月前
		} else {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断当前时间是星期几
	 * @param sdate
     * @return
     */
	public static String friendly_time2(Context context,String sdate) {
		String res = "";
		if (StringUtils.isEmpty(sdate))
			return "";

		String[] weekDays = {context.getResources().getString(R.string.str288),
				context.getResources().getString(R.string.str289),
				context.getResources().getString(R.string.str290),
				context.getResources().getString(R.string.str291),
				context.getResources().getString(R.string.str292),
				context.getResources().getString(R.string.str293),
				context.getResources().getString(R.string.str294)};
		String currentData = StringUtils.getDataTime("MM-dd");
		int currentDay = StringUtils.toInt(currentData.substring(3));
		int currentMoth = StringUtils.toInt(currentData.substring(0, 2));

		int sMoth = StringUtils.toInt(sdate.substring(5, 7));
		int sDay = StringUtils.toInt(sdate.substring(8, 10));
		int sYear = StringUtils.toInt(sdate.substring(0, 4));
		Date dt = new Date(sYear, sMoth - 1, sDay - 1);

		if (sDay == currentDay && sMoth == currentMoth) {
//			res = "今天 / " + weekDays[getWeekOfDate(new Date())];
			res = weekDays[getWeekOfDate(new Date())];
		} else if (sDay == currentDay + 1 && sMoth == currentMoth) {
//			res = "昨天 / " + weekDays[(getWeekOfDate(new Date()) + 6) % 7];
			res =  weekDays[(getWeekOfDate(new Date()) + 6) % 7];
		} else {
			if (sMoth < 10) {
				res = "0";
			}
			res += sMoth + "/";
			if (sDay < 10) {
				res += "0";
			}
//			res += sDay + " / " + weekDays[getWeekOfDate(dt)];
			res= weekDays[getWeekOfDate(dt)];
		}

		return res;
	}


	/**
	 * 智能格式化
	 */
	public static String friendly_time3(String sdate) {
		String res = "";
		if (StringUtils.isEmpty(sdate))
			return "";

		Date date = StringUtils.toDate(sdate);
		if (date == null)
			return sdate;

		SimpleDateFormat format = dateFormater2.get();

		if (isToday(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "上午 hh:mm" : "下午 hh:mm");
			res = format.format(date);
		} else if (isYesterday(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "昨天 上午 hh:mm" : "昨天 下午 hh:mm");
			res = format.format(date);
		} else if (isCurrentYear(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "MM-dd 上午 hh:mm" : "MM-dd 下午 hh:mm");
			res = format.format(date);
		} else {
			format.applyPattern(isMorning(date.getTime()) ? "yyyy-MM-dd 上午 hh:mm" : "yyyy-MM-dd 下午 hh:mm");
			res = format.format(date);
		}
		return res;
	}

	/**
	 * @return 判断一个时间是不是上午
	 */
	public static boolean isMorning(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int hour = time.hour;
		return (hour >= 0) && (hour < 12);
	}

	/**
	 * @return 判断一个时间是不是今天
	 */
	public static boolean isToday(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year)
				&& (thenMonth == time.month)
				&& (thenMonthDay == time.monthDay);
	}

	/**
	 * @return 判断一个时间是不是昨天
	 */
	public static boolean isYesterday(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year)
				&& (thenMonth == time.month)
				&& (time.monthDay - thenMonthDay == 1);
	}

	/**
	 * @return 判断一个时间是不是今年
	 */
	public static boolean isCurrentYear(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year);
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static int getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 *
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 返回long类型的今天的日期
	 *
	 * @return
	 */
	public static long getToday() {
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater2.get().format(cal.getTime());
		curDate = curDate.replace("-", "");
		return Long.parseLong(curDate);
	}

	public static String getCurTimeStr() {
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater.get().format(cal.getTime());
		return curDate;
	}

	/**
	 * 获取当前系统时间
	 * @return
     */
	public  static String getCurDate(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	public  static String getCurDateYMD(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 获取系统时间的10位的时间戳
	 * @return
     */
	public static String getCurrentTime(){

		long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳

		String  str=String.valueOf(time);

		return str;

	}
	/**
	 * 日期间隔
	 * @param date_begin
	 * @param date_end
     * @return
     */
	public  static long getDateSpan(String date_begin,String date_end,int type){
		DateFormat df = null;
		if(type==0){
			 df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}else if(type==1){
			 df = new SimpleDateFormat("yyyy-MM-dd");
		}
		long days = 0;
		try {
			Date d1 = df.parse(date_begin);
			Date d2 = df.parse(date_end);
			long diff = d2.getTime() - d1.getTime();
			 days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}
	/**
	 * 近多少天
	 * @param date_end
     * @return
     */
	public  static String getNearlyDate(String date_end,long dur){
		DateFormat df = null;
			 df = new SimpleDateFormat("yyyy-MM-dd");
		long diff = 0;
		try {
			Date d2 = df.parse(date_end);
			 diff = d2.getTime() - (dur*(1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getDateTimeFromMillisecond(diff);
	}
	public  static String getNearlyDateYMD(String date_end,long dur){
		DateFormat df = null;
			 df = new SimpleDateFormat("yyyy-MM-dd");
		long diff = 0;
		try {
			Date d2 = df.parse(date_end);
			 diff = d2.getTime() - (dur*(1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getDateTimeFromMillisecondYMD(diff);
	}
	/**
	 * 时间间隔
	 * @param date_begin
	 * @param date_end
	 * @return
	 */
	public  static long getTimeSpan(String date_begin,String date_end){
		DateFormat df = new SimpleDateFormat("HH:mm");
		long times = 0;
		try {
			Date d1 = df.parse(date_begin);
			Date d2 = df.parse(date_end);
			long diff = d2.getTime() - d1.getTime();
			times = diff / (1000 * 60 * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return times;
	}
	/***
	 * 计算两个时间差，返回的是的秒s
	 *
	 * @author 火蚁 2015-2-9 下午4:50:06
	 *
	 * @return long
	 * @param dete1
	 * @param date2
	 * @return
	 */
	public static long calDateDifferent(String dete1, String date2) {

		long diff = 0;

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = dateFormater.get().parse(dete1);
			d2 = dateFormater.get().parse(date2);

			// 毫秒ms
			diff = d2.getTime() - d1.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return diff / 1000;
	}

	/**
	 * 获取昨天日期
	 * @return
     */
	public static String getYesterdayDate(){
		Date date=new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;

	}

	/**
	 * 获取月份时间
	 * 本月：i=0;
	 * 上月：i=-1
	 * @return
     */
	public static String getMonthDate(int i){
		Date date=new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.MONTH,i);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String dateString = formatter.format(date);
		return dateString;

	}
	/**
	 * 获取当前时间标识码精确到毫秒
	 * */
	public static String getNowtimeKeyStr(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 设置日期格式
		String nowdate = df.format(new Date());// new Date()为获取当前系统时间
		return nowdate;
	}



	/**
	 * 毫秒转固定时间格式，android是13位
	 * @param millisecond
	 * @return
	 */
	public static String getDateTimeFromMilli(Long millisecond){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		Date date = new Date(millisecond);
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}

	/**
	 * 毫秒转固定时间格式，android是13位
	 * @param millisecond
	 * @return
	 */
	public static String getDateTimeFromMillisecond(Long millisecond){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(millisecond);
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}
	public static String getDateTimeFromMillisecondYMD(Long millisecond){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(millisecond);
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}
	/**
	 * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
	 *
	 * @param time
	 * @return
	 */
	public static String dataOne(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}
	/**
	 * 日期和时间一起的时间戳
	 *
	 */
	private static Calendar dateAndTime=Calendar.getInstance(Locale.CHINA);
	private static DateFormat fmtDate=new java.text.SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat fmtTime=new java.text.SimpleDateFormat("HH:mm:ss");
	public static void runTime(Context context,final TextView mEditText) {
		TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				dateAndTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
				dateAndTime.set(Calendar.MINUTE, minute);
				mEditText.setText(fmtDate.format(dateAndTime.getTime())+" "+fmtTime.format(dateAndTime.getTime()));
			}
		};

		DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				dateAndTime.set(Calendar.YEAR,year);
				dateAndTime.set(Calendar.MONTH, monthOfYear);
				dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);


			}
		};

		TimePickerDialog mTimePickerDialog=new TimePickerDialog(context, t, dateAndTime.get(Calendar.HOUR_OF_DAY),
				dateAndTime.get(Calendar.MINUTE), true);
		mTimePickerDialog.show();

		DatePickerDialog mDatePickerDialog=new DatePickerDialog(context, d, dateAndTime.get(Calendar.YEAR),
				dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
		mDatePickerDialog.show();
	}
}
