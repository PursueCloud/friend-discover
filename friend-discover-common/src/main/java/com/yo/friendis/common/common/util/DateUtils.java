package com.yo.friendis.common.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * General Utils for Solving Date
 * @author jay.huang
 *
 */
public class DateUtils {
	/**
	 * 获取当前时间戳（含时分秒
	 * @return java.sql.Timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	/**
	 * 获取当前日期的时间戳（不含时分秒
	 * @return java.sql.Timestamp
	 */
	public static Timestamp getCurrentDate() {
		Date toDay = getCurrentTimestamp();
		String toDayStr = formatTime(toDay, StringPool.PATTERN_DATE);
		return Timestamp.valueOf(toDayStr + " 00:00:00.0");
	}
	/**
	 * 根据给定毫秒格式话时间
	 */
	public static Timestamp getTimestampByMills(long Millis) {
		return new Timestamp(Millis);
	}
	/**
	 *获取今天日期
	 * @return java.sql.Date
	 */
	public static java.sql.Date getTodaySqlDate() {
		return getSqlDateByNum(-1);
	}
	/**
	 *获取明天日期
	 * @return java.sql.Date
	 */
	public static java.sql.Date getTomorrowSqlDate() {
		return getSqlDateByNum(0);
	}
	/**
	 * 根据参数获取日期，i从-1开始（如：i为-1时，获取今天日期；i为0时，获取明天日期
	 * @return java.sql.Date
	 */
	public static java.sql.Date getSqlDateByNum(int i) {
		return new java.sql.Date(System.currentTimeMillis() + 1000*60*60*24*(i+1));
	}
	/**
	 * 获取本周剩余的工作日天数 
	 * @return int 剩余天数
	 */
	public static int getRemainDaysInWeek() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(new Date());
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        if(1 == dayWeek) {  
          cal.add(Calendar.DAY_OF_MONTH, 0);  
        }  
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        int remainDays = 7-1-day;
       return remainDays;
	}
	
	/**
	 * 以“yyyy-MM-dd HH:mm:ss”格式格式化日期
	 * @param d
	 * @return
	 */
	public static String formatTime(Date d){
		return formatTime(d, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 根据给定的格式格式化时间(日期
	 * @param d
	 * @param pattern 见StringPool.PATTERN_*
	 * @return
	 */
	public static String formatTime(Date d, String pattern) {
		if(d == null) {//当传入的时间为空时
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}
	/**
	 * 根据给定的格式格式化时间(日期
	 * @param s
	 * @param pattern 见StringPool.PATTERN_*
	 * @return
	 */
	public static String formatTime(Timestamp s, String pattern) {
		if(s == null) {//当传入的时间为空时
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(s);
	}
	/**
	 * 获取今天日期的timestamp对象(年月日 00:00:00
	 */
	public static Timestamp getTodayTimestamp() {
		String todayDate = getTodaySqlDate().toString() + " 00:00:00";
		return Timestamp.valueOf(todayDate);
	}
	
	/**
	 * 根据给定的开始时间和结束时间获取所花时间，返回个数为HH:mm:ss
	 * @param startTm
	 * @param endTm
	 * @return
	 */
	public static String getSpendTimeByStartAndEnd(long startTm, long endTm) {
		if(startTm > endTm) {
			return null;
		}
		long spendTimeMillis = endTm - startTm;
		long spendTimeSecs = spendTimeMillis / 1000 ; 
		long hours = spendTimeSecs / 60 / 60;
		long mins = (spendTimeSecs - hours*60*60) / 60;
		long secs = spendTimeSecs - hours*60*60 - mins*60;
		long millis = spendTimeMillis - hours*60*60*1000 - mins*60*1000 - secs*1000;
		String hoursStr = hours + "";
		if(hoursStr.equals("0")) {
			hoursStr = "00";
		}
		if( hours < 10 ) {
			hoursStr = "0" + hours;
		}
		String minsStr = mins + "";
		if( mins < 10 ) {
			minsStr = "0" + mins;
		}
		if(minsStr.equals("0")) {
			minsStr = "00";
		}
		String secsStr = secs + "";
		if(secs < 10) {
			secsStr = "0" + secs;
		}
		String millisStr = "." + millis;
		return hoursStr + ":" + minsStr + ":" + secsStr + millisStr;
	}

	/**
	 * 获取当前beginDateTs至endDateTs之间每一天日期的字符串集合（yyyy-MM-dd格式）
	 * @param beginDateTs
	 * @param endDateTs
     * @return
     */
	public static List<String> getDayInAWeekStrListByBeginAndEndDate(Timestamp beginDateTs, Timestamp endDateTs) {
		if( beginDateTs==null || endDateTs==null ) {
			return null;
		}
		long oneDayMillis = 24*60*60*1000;//一天的毫秒数
		int days = (int)((endDateTs.getTime() - beginDateTs.getTime()) / oneDayMillis);//计算开始日期和结束日期之间相隔天数
//		String sunday = DateUtils.formatTime(beginDateTs, StringPool.PATTERN_DATE);
//		String monday = DateUtils.formatTime(new Timestamp(beginDateTs.getTime() + 1*24*60*60*1000), StringPool.PATTERN_DATE);
//		String tuesday = DateUtils.formatTime(new Timestamp(beginDateTs.getTime() + 2*24*60*60*1000), StringPool.PATTERN_DATE);
//		String wednesday = DateUtils.formatTime(new Timestamp(beginDateTs.getTime() + 3*24*60*60*1000), StringPool.PATTERN_DATE);
//		String thursday = DateUtils.formatTime(new Timestamp(beginDateTs.getTime() + 4*24*60*60*1000), StringPool.PATTERN_DATE);
//		String friday = DateUtils.formatTime(new Timestamp(beginDateTs.getTime() + 5*24*60*60*1000), StringPool.PATTERN_DATE);
//		String saturday = DateUtils.formatTime(endDateTs, StringPool.PATTERN_DATE);
		List<String> dayInAWeeksList = new ArrayList<String>();
//		dayInAWeeksList.add(sunday);
//		dayInAWeeksList.add(monday);
//		dayInAWeeksList.add(tuesday);
//		dayInAWeeksList.add(wednesday);
//		dayInAWeeksList.add(thursday);
//		dayInAWeeksList.add(friday);
//		dayInAWeeksList.add(saturday);
		for(int i=0; i<=days; i++) {
			Timestamp currDayTs = new Timestamp(beginDateTs.getTime() + i*oneDayMillis);
			dayInAWeeksList.add(formatTime(currDayTs, StringPool.PATTERN_DATE));
		}

		return dayInAWeeksList;
	}

	/**
	 * 根据日期字符串获取当前日期字符串代表的日期的毫秒数
	 * @param dateStr
	 * @return
     */
	public static long getMillisByDateStr(String dateStr) {
		if( org.apache.commons.lang.StringUtils.isBlank(dateStr) ) {
			return 0;
		}
		Timestamp date = getTimestampByDateStr(dateStr);
		return date.getTime();
	}
	/**
	 *根据日期字符串获取当前日期字符串代表的日期
	 */
	public static Timestamp getTimestampByDateStr(String dateStr) {
		if( org.apache.commons.lang.StringUtils.isBlank(dateStr) ) {
			return null;
		}
		Timestamp date = Timestamp.valueOf(dateStr + " 00:00:00.0");
		return date;
	}
	/**
	 * 根据日期字符串获取当前日期字符串代表的日期是周几
	 */
	public static String getDayInWeekByDateStr(String dateStr) {
		if( org.apache.commons.lang.StringUtils.isBlank(dateStr) ) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		Timestamp date = Timestamp.valueOf(dateStr + " 00:00:00.0");
		calendar.setTime(date);
		int indexOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if( indexOfWeek != -1 ) {
			if( indexOfWeek == 1 ) {
				return "周日";
			} else if( indexOfWeek == 2 ) {
				return "周一";
			} else if( indexOfWeek == 3 ) {
				return "周二";
			} else if( indexOfWeek == 4 ) {
				return "周三";
			} else if( indexOfWeek == 5 ) {
				return "周四";
			} else if( indexOfWeek == 6 ) {
				return "周五";
			} else if( indexOfWeek == 7 ) {
				return "周六";
			}
		}
		return "";
	}
}
