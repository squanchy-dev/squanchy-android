package com.ls.utils;

import android.support.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static boolean isToday(long millis) {
		boolean isToday = false;

		Calendar currCalendar = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);

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

	@Nullable
	public static String convertDateTo24Format(@NotNull String strDate) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (date != null) {
			SimpleDateFormat newFormat = new SimpleDateFormat("kk:mm");
			return newFormat.format(date.getTime());
		} else {
			return strDate;
		}
	}

	@Nullable
	public static Date convertDate(String strDate) {
		Date date;

		if (strDate.toLowerCase().contains("pm") || strDate.toLowerCase().contains("am")) {
			SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
			try {
				date = format.parse(strDate);
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			SimpleDateFormat format = new SimpleDateFormat("kk:mm");
			try {
				date = format.parse(strDate);

				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getWeekDay(long millis) {
		SimpleDateFormat format = new SimpleDateFormat("EEE");
		return format.format(new Date(millis));
	}

    public static long convertWeekDayToLong(String weekDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
        Date date;
        try {
            date = dateFormat.parse(weekDay);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
