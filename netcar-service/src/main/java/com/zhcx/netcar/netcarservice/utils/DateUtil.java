package com.zhcx.netcar.netcarservice.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 * 
 * @title
 * @author 龚进
 * @date 2017年9月1日
 * @version 1.0
 */
public class DateUtil extends DateUtils {

	private final static String FORMAT_yyyy_MM = "yyyy-MM";
	private final static String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
	private final static String FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	private final static String FORMAT_HH_mm = "HH:mm";
	private final static String FORMAT_HH_mm_ss = "HH:mm:ss";
	private final static String FORMAT_yyyy_MM_dd_HH_mm_ss_SSS = "yyyyMMddHHmmssSSS";
	
	private final static SimpleDateFormat formatYYYYMM = new SimpleDateFormat(FORMAT_yyyy_MM);
	private final static SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat(FORMAT_yyyy_MM_dd);
	private final static SimpleDateFormat formatYYYYMMDDHHMMSS = new SimpleDateFormat(FORMAT_yyyy_MM_dd_HH_mm_ss);
	private final static SimpleDateFormat formatYYYYMMDDHHMMSSSSS = new SimpleDateFormat(FORMAT_yyyy_MM_dd_HH_mm_ss_SSS);

	public static String getCurrMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return formatYYYYMM.format(calendar.getTime());
	}

	public static String getCurrDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return formatYYYYMMDD.format(calendar.getTime());
	}
	
	public static String getCurrMills() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return formatYYYYMMDDHHMMSSSSS.format(calendar.getTime());
	}

	public static String getNextMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, 1);
		return formatYYYYMM.format(calendar.getTime());
	}

	public static String getNextDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return formatYYYYMMDD.format(calendar.getTime());
	}
	
    public static Date getStartTime() {  
        Calendar todayStart = Calendar.getInstance();  
        todayStart.set(Calendar.HOUR, 0);  
        todayStart.set(Calendar.MINUTE, 0);  
        todayStart.set(Calendar.SECOND, 0);  
        todayStart.set(Calendar.MILLISECOND, 0);  
        return todayStart.getTime();  
    }  
  
    public static Date getEndTime() {  
        Calendar todayEnd = Calendar.getInstance();  
        todayEnd.set(Calendar.HOUR, 23);  
        todayEnd.set(Calendar.MINUTE, 59);  
        todayEnd.set(Calendar.SECOND, 59);  
        todayEnd.set(Calendar.MILLISECOND, 999);  
        return todayEnd.getTime();  
    }
    
    public static String getDateFormatYmd(Date date) {  
    	    SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");  
    	    return formatter.format(date);
    }

	public static int compareDate(String date1, String date2) {
		Date dt1 = null, dt2 = null;
		try {
			dt1 = formatYYYYMM.parse(date1);
			dt2 = formatYYYYMM.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (dt1.getTime() > dt2.getTime()) {
			//System.out.println("dt1 在dt2前");
			return 1;
		} else if (dt1.getTime() < dt2.getTime()) {
			//System.out.println("dt1在dt2后");
			return -1;
		} else {
			return 0;
		}
	}
	
	public static int compareHM(String date1, String date2) {
		Integer dt1 = getMinutes(date1,":");
		Integer dt2 = getMinutes(date2,":");
		if (dt1 > dt2) {
			return 1;
		} else if (dt1 < dt2) {
			return -1;
		} else {
			return 0;
		}
	}

	public static String getLastDay(String month) {
		Calendar calendar = Calendar.getInstance();
		Date date = null;
		try {
			date = formatYYYYMM.parse(month);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int maxday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, maxday);
		return formatYYYYMMDD.format(calendar.getTime());
	}

	public static String getFirstDay(String month) {
		Calendar calendar = Calendar.getInstance();
		Date date = null;
		try {
			date = formatYYYYMM.parse(month);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int minday = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, minday);
		return formatYYYYMMDD.format(calendar.getTime());
	}
	
	
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH)+1;
		return month;
	}
	
	public static String getYMDFormat(Date date) {
		if (date == null) {
			date = new Date();
		}
		return DateFormatUtils.format(date, FORMAT_yyyy_MM_dd);
	}
	
	public static String getYMFormat(String date) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(formatYYYYMM.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatYYYYMM.format(cal.getTime());
	}
	
	public static Date getYMDFormat(String date) {
		if (StringUtils.isNotBlank(date)) {
			try {
				return parseDate(date, FORMAT_yyyy_MM_dd);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	public static String plus(Date date, int day) {
		//SimpleDateFormat format = new SimpleDateFormat(FORMAT_yyyy_MM_dd);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return formatYYYYMMDD.format(cal.getTime());
	}

	public static List<String> getDaysByYearMonth(String yearMonth) {
		//SimpleDateFormat format = new SimpleDateFormat(FORMAT_yyyy_MM_dd);
		String[] str = yearMonth.split("-");
		if (str.length != 2) {
			return null;
		}
		int year = Integer.valueOf(str[0]);
		int month = Integer.valueOf(str[1]);

		List<String> dayList = new ArrayList<String>();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		int temp = 0;
		do {
			dayList.add(formatYYYYMMDD.format(cal.getTime()));
			cal.add(Calendar.DAY_OF_MONTH, 1);
			temp = cal.get(Calendar.MONTH) + 1;
		} while (month == temp);
		return dayList;
	}

	public static int dayForMonth(String date) {
		//SimpleDateFormat format = new SimpleDateFormat(FORMAT_yyyy_MM_dd);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(formatYYYYMMDD.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int dayForWeek(String date) {
		//SimpleDateFormat format = new SimpleDateFormat(FORMAT_yyyy_MM_dd);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(formatYYYYMMDD.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int dayForWeek = 0;
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getYMDHMSFormat(Date date) {
		if (date == null) {
			date = new Date();
		}
		return DateFormatUtils.format(date, FORMAT_yyyy_MM_dd_HH_mm_ss);
	}

	public static Date getYMDHMSFormat(String date) {
		if (StringUtils.isNotBlank(date)) {
			try {
				return parseDate(date, FORMAT_yyyy_MM_dd_HH_mm_ss);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	/**
	 * 时间转化为分钟
	 * 
	 * @param date
	 * @param str
	 * @return
	 */
	public static Integer getMinutes(String date, String str) {
		String[] strs = date.split(str);
		int hour = Integer.parseInt(strs[0]);
		int min = Integer.parseInt(strs[1]);
		int seconds = hour * 60 + min;
		return seconds;
	}
	public static Integer getMinutes(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int seconds = hour * 60 + min;
		return seconds;
	}
	public static Integer getSeconds(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int seconds = (hour * 60 + min) * 60 + sec;
		return seconds;
	}
	public static Integer getDay(String date) {
		//SimpleDateFormat format = new SimpleDateFormat(FORMAT_yyyy_MM_dd);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(formatYYYYMMDD.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 整数(秒数)转换为时分秒格式(xx:xx:xx)
	 * 
	 * @param time
	 * @return
	 */
	public static String secToTime(Integer time) {
		if (null == time) {
			return "--";
		}
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	/**
	 * 获取当前月
	 * 
	 * @return
	 */
	public static int getNowMonth() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 把时间保留时分
	 * @param date
	 * @return
	 */
	public static Date reserveHm(Date date) {
		if(null == date) {
			return null;
		}
		Calendar calParam = Calendar.getInstance();
		calParam.setTime(date);
		Date now = new Date();
		Calendar calResult = Calendar.getInstance();
		calResult.setTime(now);
		calResult.set(Calendar.HOUR_OF_DAY, calParam.get(Calendar.HOUR_OF_DAY));
		calResult.set(Calendar.MINUTE, calParam.get(Calendar.MINUTE));
		calResult.set(Calendar.SECOND, 00);
		calResult.set(Calendar.MILLISECOND, 0);
		return calResult.getTime();
	}

	public static Date getDateByHM(String date){
		SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_HH_mm);
		try {
			Date today=sdf.parse(date);
			return reserveHm(today);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String getDateByHM(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_HH_mm);
		return sdf.format(date);
	}
	public static String getHMSFormat(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_HH_mm_ss);
		return sdf.format(date);
	}
	
	/**
	 * 获取时分
	 * @param date
	 * @return
	 */
	public static String getHm(Date date) {
		if(null == date) {
			return "";
		}
		StringBuilder hm = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if(hour < 10) {
			hm.append("0");
		}
		hm.append(hour).append(":");
		if(minute < 10) {
			hm.append("0");
		}
		hm.append(minute);
		return hm.toString();
	}
	
	/**
	 * 把毫秒数转为时间
	 * @param timestamp
	 * @return
	 */
	public static String convertDateStr(long timestamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		String dateStr = formatYYYYMMDDHHMMSS.format(cal.getTime());
		return dateStr;
	}
	
	/**
	 * 获取24点时间
	 */
	public static Date getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 获取0点时间
	 */
	public static Date getTimesMorning(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 获取24点时间
	 */
	public static Date getTimesnight(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static String getYYYYMMDD() {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		String date = yyyyMMdd.format(new Date());
		return date;
	}
	
	public static String getHHMMSS() {
		SimpleDateFormat hhmmss = new SimpleDateFormat("HHmmss");
		String date = hhmmss.format(new Date());
		return date;
	}

	/**
	 * 获取当前时间，前number个小时,从0点开始
	 * @param number
	 * @return
	 */
	public static Map<String, Object> getPreHoursFromZero(Integer number){
		List<String> list = new ArrayList();
		Map<String, Object> result = new LinkedHashMap<>();
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		Calendar calendar = Calendar.getInstance();
		for(int i = 0; i < number; i++){
			list.add(sdf.format(current));
			calendar.setTime(current);
			if(calendar.get(Calendar.HOUR_OF_DAY) == 0){
				break;
			}
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			current = calendar.getTime();
		}
		for(int i = list.size(); i > 0; i--){
			result.put(list.get(i - 1), 0);
		}
		return result;
	}
	
	
	/**
	 * 获取当前日期，前number天
	 * @param number
	 * @return
	 */
	public static Map<String, Object> getPreDaysFromZero(Integer number){
		List<String> list = new ArrayList();
		Map<String, Object> result = new LinkedHashMap<>();
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		for(int i = 0; i < number; i++){
			list.add(sdf.format(current));
			calendar.setTime(current);
			if(calendar.get(Calendar.DAY_OF_MONTH) == 0){
				break;
			}
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			current = calendar.getTime();
		}
		for(int i = list.size(); i > 0; i--){
			result.put(list.get(i - 1), 0);
		}
		return result;
	}

	/**
	 * 获取当前时间，前number个小时
	 * @param number
	 * @return
	 */
	public static Map<String, Object> getPreHours(Integer number){
		Map<String, Object> result = new LinkedHashMap<>();
		List<String> list = new ArrayList();
		Date current = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		Calendar calendar = Calendar.getInstance();
		for(int i = 0; i < number; i++){
			list.add(sdf.format(current));
			calendar.setTime(current);
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			current = calendar.getTime();
		}
		for(int i = list.size(); i > 0; i--){
			result.put(list.get(i - 1), 0);
		}
		return result;
	}
    public static Date getSomeDay(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i);
        Date time = calendar.getTime();
        return time;
    }

    public static Date subDay(Date date,int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -i);
        Date time = calendar.getTime();
        return time;
    }

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,24);
		System.out.println(getTimesMorning(cal.getTime())); 
		System.out.println(getTimesnight(cal.getTime()));
		System.out.println(DateUtil.getPreHours(12));
		System.out.println(DateUtil.getPreHoursFromZero(19));
	}


}
