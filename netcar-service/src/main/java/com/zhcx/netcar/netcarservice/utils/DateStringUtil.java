package com.zhcx.netcar.netcarservice.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author lisai
 */
public class DateStringUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date,String formatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00"+ ":" + "00";
        }
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10){
            retStr = "0" + Integer.toString(i);
        }
        else {
            retStr = "" + i;
        }
        return retStr;
    }
    /**
     * 获取当前月8点的时间戳
     * @param timeZone
     * @return
     */
    public static Long getCurrentYearMonth(Long curTime,String timeZone) {
        String tz = StringUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(curTime);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当天（按当前传入的时区）00:00:00所对应时刻的long型值
     * @param now
     * @param timeZone
     * @return
     */
    public static long getStartTimeOfDay(long now, String timeZone) {
        String tz = StringUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当天（按当前传入的时区）00:00:00所对应时刻的long型值
     * @param now
     * @param timeZone
     * @return
     */
    public static long getEndTimeOfDay(long now, String timeZone) {
        String tz = StringUtils.isEmpty(timeZone) ? "GMT+8" : timeZone;
        TimeZone curTimeZone = TimeZone.getTimeZone(tz);
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

}
