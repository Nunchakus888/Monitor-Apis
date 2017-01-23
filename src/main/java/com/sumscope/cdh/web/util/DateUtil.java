package com.sumscope.cdh.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wenshuai.li on 2015/11/22.
 */
public class DateUtil {
    public final static String START_TIME_SURFIX = "00:00:00";
    public final static String END_TIME_SURFIX = "23:59:59";

    static Date today = new Date();

    public static SimpleDateFormat yyyyMMddHHmmssSS = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SS");

    public static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    public static SimpleDateFormat HHmmss = new SimpleDateFormat("HH:mm:ss");

    public static int daysToTerm(String term){
        String strUp = term.toUpperCase();
        if ("".equals(strUp)){
            return 0;
        }else if (strUp.contains("Y")){
            if (strUp.split("Y") == null){
                return 0;
            }else {
                int year = Integer.parseInt(strUp.split("Y")[0]);
                Date newDate = new Date(today.getYear() + year, today.getMonth(), today.getDate());
                return daysFromToday(newDate);
            }
        }else if (strUp.contains("M")){
            if (strUp.split("M") == null){
                return 0;
            }else {
                int month = Integer.parseInt(strUp.split("M")[0]);
                Date newDate = new Date(today.getYear(), today.getMonth() + month, today.getDate());
                return daysFromToday(newDate);
            }
        }else if (strUp.contains("D")){
            if (strUp.split("D") == null){
                return 0;
            }else{
                int day = Integer.parseInt(strUp.split("D")[0]);
                return day;
            }
        }else {
            int year = Integer.parseInt(strUp);
            Date newDate = new Date(today.getYear() + year, today.getMonth(), today.getDate());
            return daysFromToday(newDate);
        }
    }

    public static int daysFromToday(Date toDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(toDate);
        long toTime = aCalendar.getTimeInMillis();
        aCalendar.setTime(today);
        long fromTime = aCalendar.getTimeInMillis();
        int between_days = (int)((toTime - fromTime)/(1000*3600*24));
        return between_days < 0 ? 0 : between_days;
    }

    public static Date StringToDate(String str) {
        DateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date=dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long dateOrtoday(String dd,String surfix) throws ParseException {
        String todayStart = null;
        if(dd == null || "".equals(dd)){
            String date = yyyyMMdd.format(new Date());
            todayStart = date+" " + surfix;

        }else {
            todayStart = dd+" " + surfix;
        }

        long d = yyyyMMddHHmmss.parse(todayStart).getTime();
        return d;
    }

    public static String yesterday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
        return yesterday;
    }

    public static String lastMonth(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,-1);
        String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
        return yesterday;
    }

    /**
     * @param year
     * @return Date 当年的第一天
     */
    public static Date getFirstDayOfYear(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(calendar.YEAR, year);
        Date firstDayOfYear = calendar.getTime();
        return firstDayOfYear;
    }

    /**
     *
     * @param date 需要格式化的
     * @return '20151223'
     */
    public static String formatDateToString(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }

    public static String y4m2d2h2m2s22h2m2s2 (String lastVisited) {
        String result = null;
        try {
            result = HHmmss.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss").parse(lastVisited));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDatetime (Object millseconds) {
        String result = null;
        try {
            result = yyyyMMddHHmmss.format(millseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
