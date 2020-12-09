package com.zhcx.authorization.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/6/11 10:24
 * 报表统计统计类
 **/
@Component
public class StatisticalUtil {

    private static String YEAR_FORMAT = "yyyy";
    private static String MONTH_FORMAT = "yyyy-MM";
    private static String DAY_FORMAT = "yyyy-MM-dd";

    public List<Map<String, Object>> dataInit(String dateStr, String dateType) {
        List result = null;
        switch (dateType) {
            case "year":
                result = getMonthListOfYear(StringToDate(dateStr, YEAR_FORMAT));
                break;
            case "month":
                //判断月份
                result = getDayListOfMonth(StringToDate(dateStr, MONTH_FORMAT));
                break;
            case "day":
                result = getHourListOfDay(StringToDate(dateStr, DAY_FORMAT));
                break;
            default:
        }
        return result;
    }

    /**
     * 获取当前月每天的日期
     * @return
     */
    public static List getDayListOfMonth(Date date) {
        List list = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //年份
        int year = calendar.get(Calendar.YEAR);
        //月份
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.getActualMaximum(Calendar.DATE);
        String yearStr = String.valueOf(year);
        String monthStr = "";
        String dayStr = "";
        if(month < 10){
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        for (int i = 1; i <= day; i++) {
            Map map = Maps.newHashMap();
            if (i < 10) {
                dayStr = "0" + i;
            } else {
                dayStr = "" + i;
            }
            String aDate = yearStr + "-" + monthStr + "-" + dayStr;
            map.put("total", 0);
            map.put("moment", aDate);
            list.add(map);
        }
        return list;
    }

    /**
     * 获取指定年的每月日期
     * @return
     */
    public static List getMonthListOfYear(Date date) {
        List list = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        //年份
        int year = calendar.get(Calendar.YEAR);
        String yearStr = String.valueOf(year);
        String monthStr = "";
        for (int i = 1; i <= 12; i++) {
            Map map = Maps.newHashMap();
            if(i < 10){
                monthStr = "0" + i;
            } else {
                monthStr = "" + i;
            }
            String aDate = yearStr + "-" + monthStr;
            map.put("total", 0);
            map.put("moment", aDate);
            list.add(map);
        }
        return list;
    }

    /**
     * 获取指定天每小时的日期
     * @return
     */
    public static List getHourListOfDay(Date date) {
        List list = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        String yearStr = "";
        String monthStr = "";
        String dayStr = "";
        String hourStr = "";
        //年份
        int year = calendar.get(Calendar.YEAR);
        yearStr = String.valueOf(year);
        //月份
        int month = calendar.get(Calendar.MONTH) + 1;
        if(month < 10){
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        int day = calendar.get(Calendar.DATE);
        if(day < 10){
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        for (int i = 1; i < 24; i++) {
            Map map = Maps.newHashMap();
            if(i < 10){
                hourStr = "0" + i;
            } else {
                hourStr = "" + i;
            }
            String aDate = yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr;
            map.put("total", 0);
            map.put("moment", aDate);
            list.add(map);
        }
        return list;
    }

    /**
     * 获取当前月每天的日期
     * @return
     */
    public static List<Map<String,Object>> getSomeDayListOfMonth(Date finalStartDate,Date finalEndDate) {
        List<Map<String,Object>> list = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        while (finalEndDate.getTime() >= finalStartDate.getTime()) {
            String moment = DateUtil.getYMDFormat(finalStartDate);
            Map<String,Object> map = Maps.newHashMap();
            map.put("total", 0);
            map.put("moment", moment);
            list.add(map);

            calendar.setTime(finalStartDate);
            calendar.add(Calendar.DATE, 1);
            finalStartDate = calendar.getTime();
        }
        return list;
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


}
