package com.connfa.utils;

import android.content.Context;
import android.text.format.DateFormat;

import com.connfa.model.PreferencesManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static TimeZone getTimeZone(Context context) {
        return PreferencesManager.create(context).getServerTimeZoneObject();
    }

    public static Date convertEventDayDate(Context context, String day) {
        SimpleDateFormat dateFormat = getDateFormat("d-MM-yyyy");
        dateFormat.setTimeZone(getTimeZone(context));

        try {
            return dateFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SimpleDateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.ENGLISH);
    }

    public static boolean isToday(Context context, long millis) {
        TimeZone timeZone = getTimeZone(context);

        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeZone(timeZone);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.setTimeZone(timeZone);

        int todayYear = todayCalendar.get(Calendar.YEAR);
        int todayMonth = todayCalendar.get(Calendar.MONTH);
        int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return (todayYear == year && todayMonth == month && todayDay == day);
    }

    public static boolean isAfterCurrentDate(long millis) {
        return millis > System.currentTimeMillis();
    }

    public static String getTime(Context context, long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.setTimeZone(getTimeZone(context));

        if (DateFormat.is24HourFormat(context)) {
            return getDateFormat("HH:mm").format(new Date(millis));
        } else {
            return getDateFormat("hh:mm aa").format(new Date(millis));
        }
    }

    public static String getWeekDay(Context context, long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.setTimeZone(getTimeZone(context));

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
    }

    public static String getWeekNameAndDate(long millis) {
        return getDateFormat("EEE d").format(new Date(millis));
    }
}
