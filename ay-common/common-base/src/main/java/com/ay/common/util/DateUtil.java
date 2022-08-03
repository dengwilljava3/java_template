package com.ay.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateUtil {

	public static String YY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static String YY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static String YY_MM_DD = "yyyy-MM-dd";
	public static String YY_MM_DD1 = "yyyyMMdd";
	public static String YYMMDD = "yyyyMMdd";
	public static String MM_DD_HH_MM = "MM-dd HH:mm";
	public static String YY_MM_DD_HH_MM_SS_2 = "yyyy/MM/dd HH:mm:ss";
	public static String YY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
	public static String YY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static String yy_MM_DD_T_HH_MM_SS_XXX = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static String YYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static String YYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	public static String YYMMDDHHMMSSXXX = "yyyy-MM-dd'T'HH:mm:ssXXX"; // 2018-05-24T22:50:35-04:00
	public static String YYMMDDHHMMSSXXXZ = "yyyy-MM-dd'T'HH:mm:ssXXXZ"; // 2018-05-24T22:50:35-04:00
	public static String YY_MM_DD_CHINA = "yyyy年MM月dd日";

	public static final String GMT_4 = "GMT-4"; // 美东时间
	public static final String GMT_8 = "GMT+8"; // 中国时间

	public static SimpleDateFormat sdf1 = new SimpleDateFormat(YY_MM_DD1);
	public static SimpleDateFormat sdf2 = new SimpleDateFormat(YY_MM_DD_HH_MM_SS);

	public static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
		Date dateTmp = null;
		if (date != null) {
			int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
			dateTmp = new Date(date.getTime() - timeOffset);
		}
		return dateTmp;
	}

	// 获取当前时间
	public static Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone(DateUtil.GMT_8));
		return c.getTime();
	}

	// 获取当前时间
	public static String getCurDate() {
		return sdf1.format(new Date(System.currentTimeMillis()));
	}

	public static String getCurTime() {
		return sdf2.format(new Date(System.currentTimeMillis()));
	}

	// 根据毫秒数获取时间
	public static String getDateByMills(Long mills) {
		if (mills == null) {
			return null;
		}
		return sdf1.format(new Date(mills));
	}

	// 根据秒数获取时间
	public static String getDateBySeconds(Integer second) {
		if (second == null) {
			return null;
		}
		return getDateByMills(Long.valueOf(second + "000"));
	}

	public static String format(String format, Date date) {
		return format(format, date, TimeZone.getTimeZone(DateUtil.GMT_8));
	}

	public static String format(String format, Date date, TimeZone timeZone) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.format(date);
	}

	public static Date parse(String format, String date) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据连接符join 连接日期
	 *
	 * @param differDay 相差的天数
	 * @param join      2016-05-31
	 */
	public static String getDaysAgo(int interval, String join) {
		Date date = new Date();
		long time = (date.getTime() / 1000) - interval * 60 * 60 * 24;
		date.setTime(time * 1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy" + join + "MM" + join + "dd");
		try {
			return format.format(date);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return "";
	}

	public static String getFormatedDateString(float timeZoneOffset) {
		if (timeZoneOffset > 13 || timeZoneOffset < -12) {
			timeZoneOffset = 0;
		}

		int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
		TimeZone timeZone;
		String[] ids = TimeZone.getAvailableIDs(newTime);
		if (ids.length == 0) {
			timeZone = TimeZone.getDefault();
		} else {
			timeZone = new SimpleTimeZone(newTime, ids[0]);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(timeZone);
		return sdf.format(new Date());
	}

	/**
	 * 根据连接符join 连接日期
	 *
	 * @param differDay 相差的天数
	 * @param join      2016-05-31
	 */
	public static String getTodayDateDifferDay(int differDay, String join) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH) + differDay;
		return year + join + (month < 10 ? "0" + month : month) + join + (day < 10 ? "0" + day : day);
	}

	/**
	 * 解析2017-02-25 16:16:27+08:00<br>
	 * 返回YYY-MM-DD HH:mm:ss
	 */
	public static String parseFormatByAdd(String date) {
		if (StringUtil.isNull(date)) {
			return null;
		}
		int index = date.indexOf("+");
		return date.substring(0, index);
	}

	/**
	 * 现在相差的毫秒数
	 */
	public static Date getNowDiffer(long differAgo) {
		return new Date(System.currentTimeMillis() - differAgo);
	}

	/**
	 * 时间转换格式
	 */
	public static String timeSwitchDateStrByTwilio(String dateStr) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").parse(dateStr);
		String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
		return format;
	}

	public static Date timeSwitchDateByTwilio(String dateStr) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").parse(dateStr);
		return date;
	}

	public static Date dateDiffer(Date date, int days) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 获取今天凌晨的毫秒数
	 * 
	 * @return
	 */
	public static Long getTodayTimes() {
		return parse(YYMMDD, getTodayDateDifferDay(0, "")).getTime();
	}

	/**
	 * 根据毫秒数获取时分秒格式
	 * 
	 * @param mills 毫秒数
	 * @return 时分秒格式化
	 */
	public static String getHHmmssByMills(Long mills) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mills);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		return (hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
	}

	public static String getGapTime(long time) {
		long hours = time / (1000 * 60 * 60);
		long minutes = (time - hours * (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (time / 1000) % 60;
		String diffTime = "";
		if (minutes < 10) {
			diffTime = hours + ":0" + minutes;
		} else {
			diffTime = hours + ":" + minutes;
		}
		diffTime += ":" + (seconds < 10 ? "0" + seconds : seconds);
		return diffTime;
	}

	public static void main(String[] args) throws Exception {
		long now1 = System.currentTimeMillis();
		Thread.sleep(10 * 1000);
		long now2 = System.currentTimeMillis();
		System.out.println(getGapTime((now2 - now1) * 1000l));
	}

	public static Long getMillsByHHmmss(String hhmmss, String split) {
		String[] arr = hhmmss.split(split);
		if (arr == null || arr.length < 3) {
			return null;
		}
		return Long.valueOf(arr[0]) * 60 * 60 * 1000 + Long.valueOf(arr[1]) * 60 * 1000 + Long.valueOf(arr[2]) * 1000;
	}

	/**
	 * 根据时区获取日期yyyMMdd
	 */
	public static String getCurDateStrByTimeZone(String format, String timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdf.format(System.currentTimeMillis());
	}

	/**
	 * 获取某天的零点零分零秒
	 * 
	 * @return
	 */
	public static Date getDayFirstSecond(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + day, 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某天的零点零分零秒
	 * 
	 * @return
	 */
	public static Date getHourDiffer(int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), //
				calendar.get(Calendar.HOUR_OF_DAY) + hour, //
				calendar.get(Calendar.MINUTE) + minute, //
				calendar.get(Calendar.SECOND) + second);
		return calendar.getTime();
	}

	/**
	 * 获取当前的23点59分59秒
	 * 
	 * @return
	 */
	public static Date getDayLastSecond(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + day, 23, 59, 59);
		return calendar.getTime();
	}

	/**
	 * 获取昨天第一秒
	 * 
	 * @return
	 */
	public static Date getYesterdayFirstSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * 获取昨天最后一秒
	 * 
	 * @return
	 */
	public static Date getYesterdayLastSecond() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 23, 59, 59);
		return calendar.getTime();
	}

	public static int getDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 根据时间戳获取格式时间
	 * 
	 * @param millis
	 * @param minute
	 * @return
	 */
	public static String getFormatByMillis(Long millis, String format) {
		return getFormatByMillis(millis, format, TimeZone.getTimeZone(GMT_8));
	}

	public static String getFormatByMillis(Long millis, String format, TimeZone timeZone) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(millis);
		return DateUtil.format(format, c.getTime(), timeZone);
	}

	/**
	 * 获取某个月的开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthStart(Date date) {
		return getMonthStartByMonthCount(date, 0);
	}

	/**
	 * 获取某个月的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		return getMonthEndByMonthCount(date, 0);
	}

	/**
	 * 获取时间相差月的第一天
	 * 
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date getMonthStartByMonthCount(Date date, int count) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, count);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取时间相差月的最后一天
	 * 
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date getMonthEndByMonthCount(Date date, int count) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, count + 1);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
}
