package com.xzcf.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat dateTimeSDF = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());

    public static String toDateString(Date date) {
        if (date == null) {
            return "";
        }
        return dateSDF.format(date);
    }

    public static String toDateTimeString(Date date) {
        if (date == null) {
            return "";
        }
        return dateTimeSDF.format(date);
    }

    public static Date addDate(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }
}
