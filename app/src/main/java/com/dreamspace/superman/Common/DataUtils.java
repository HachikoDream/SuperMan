package com.dreamspace.superman.Common;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wells on 2015/11/19.
 */
public class DataUtils {
    public static final long DAY_MS = 24 * 3600 * 1000;
    public static final long HOUR_MS = 3600 * 1000;
    public static final long MINUTE_MS = 60 * 1000;
    public static final String JUST_NOW = "刚刚";
    public static final String AFTER_DAY = "天前";
    public static final String AFTER_MINUTE = "分钟前";
    public static final String AFTER_HOUR = "小时前";
    public static final String MONTH_NAME = "月";
    public static final String DAY_NAME = "日";

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        return month + 1;
    }

    public static int getCurrentYear() {
        return getYear(new Date());
    }

    public static int getCurrentMonth() {
        return getMonth(new Date());
    }

    public static long betweenmillonsecond(Date date1, Date date2) {
        long day2 = date2.getTime();
        long day1 = date1.getTime();
        long betweenseconds = Math.abs(day2 - day1);
        return betweenseconds;
    }

    public static String getDesWithOutYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.MONTH)).append(MONTH_NAME).append(calendar.get(Calendar.DAY_OF_MONTH)).append(DAY_NAME);
        return sb.toString();
    }

    public static int betweenDays(Date date1, Date date2) {

        return (int) (betweenmillonsecond(date1, date2) / DAY_MS);
    }

    public static int betweenHours(Date date1, Date date2) {
        return (int) (betweenmillonsecond(date1, date2) / HOUR_MS);
    }

    public static int betweenMinute(Date date1, Date date2) {
        return (int) (betweenmillonsecond(date1, date2) / MINUTE_MS);
    }
    public static String getTag(Date date){
        int day=betweenDays(date,new Date());
        if(day<1){
           int hour=betweenHours(date,new Date());
           if (hour<1){
              int minute=betweenMinute(date,new Date());
               if(minute<=5){
                   return JUST_NOW;
               }else{
                   return new StringBuilder().append(minute).append(AFTER_MINUTE).toString();
               }
           }else{
               return new StringBuilder().append(hour).append(AFTER_HOUR).toString();
           }
        }else if (day>10){
          return getDesWithOutYear(date);
        }else{
            return new StringBuilder().append(day).append(AFTER_DAY).toString();
        }
    }

}
