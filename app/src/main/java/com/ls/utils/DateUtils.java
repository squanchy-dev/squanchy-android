package com.ls.utils;

import android.support.annotation.Nullable;

import com.ls.drupalconapp.model.PreferencesManager;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private SimpleDateFormat mDateFormat;
    private TimeZone mTimezone;
    private static DateUtils mUtils;

    public DateUtils() {
        mDateFormat = new SimpleDateFormat();
        String timeZone = PreferencesManager.getInstance().getTimeZone();
        mTimezone = TimeZone.getTimeZone(timeZone);
        mDateFormat.setTimeZone(mTimezone);
    }

    @NotNull
    public static DateUtils getInstance() {
        if (mUtils == null) {
            mUtils = new DateUtils();
        }
        return mUtils;
    }

    @Nullable
    public Date convertEventDayDate(String day) {
        mDateFormat.applyPattern("d-MM-yyyy");

        try {
            return mDateFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public Date convertTime(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date;

        if (strDate.toLowerCase().contains("pm") || strDate.toLowerCase().contains("am")) {
            dateFormat.applyPattern("hh:mm aa");

            try {
                date = dateFormat.parse(strDate);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            dateFormat.applyPattern("kk:mm");
            try {
                date = dateFormat.parse(strDate);

                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isToday(long millis) {
        boolean isToday = false;

        Calendar currCalendar = Calendar.getInstance();
        currCalendar.setTimeZone(mTimezone);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.setTimeZone(mTimezone);

        int currYear = currCalendar.get(Calendar.YEAR);
        int currMonth = currCalendar.get(Calendar.MONTH);
        int currDay = currCalendar.get(Calendar.DAY_OF_MONTH);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (currYear == year && currMonth == month && currDay == day) {
            isToday = true;
        }
        return isToday;
    }

    public int getHours() {
        String zone = PreferencesManager.getInstance().getTimeZone();
        TimeZone timeZone = TimeZone.getTimeZone(zone);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public String get24HoursTime(@NotNull String strDate) {
        mDateFormat.applyPattern("hh:mm aa");
        Date date = null;

        try {
            date = mDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            mDateFormat.applyPattern("kk:mm");
            return mDateFormat.format(date.getTime());
        } else {
            return strDate;
        }
    }

    public String getWeekDay(long millis) {
        mDateFormat.applyPattern("EEE");
        return mDateFormat.format(new Date(millis));
    }

    public long convertWeekDayToLong(String weekDay) {
        mDateFormat.applyPattern("EEE");
        Date date;
        try {
            date = mDateFormat.parse(weekDay);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getWeekNameAndDate(long millis) {
        mDateFormat.applyPattern("EEE d");
        return mDateFormat.format(new Date(millis));
    }

    public TimeZone getTimeZone() {
        return mTimezone;
    }
}
