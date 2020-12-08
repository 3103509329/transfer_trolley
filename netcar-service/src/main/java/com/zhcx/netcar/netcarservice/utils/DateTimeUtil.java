package com.zhcx.netcar.netcarservice.utils;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static net.sf.jsqlparser.expression.DateTimeLiteralExpression.*;

/**
 * @author lisai
 */
public class DateTimeUtil extends DateUtils {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     *
     * @param date
     * @return
     */
    public static String getYMDHMSFormat(Date date) {
        if (date == null) {
            date = new Date();
        }
        return DateFormatUtils.format(date, STANDARD_FORMAT);
    }

    public static Date getCurrentHourAgo(Date date,int hour){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
        return calendar.getTime();
    }
    /**
     * @param date
     * @return
     */
    public static String getYMDHMSFormat(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        return DateFormatUtils.format(date, format);
    }

    public static Date StringToDate(String date,String format) {
        if (StringUtils.isNotBlank(date)) {
            try {
                return parseDate(date, format);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    /**
     * 获取当前月 【1日8点】的时间戳
     * @param timeZone
     * @return
     */
    public static Long getCurrentMonthPartitionTime(Long curTime,String timeZone) {
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

    public static void main(String[] args) {
    }

}
