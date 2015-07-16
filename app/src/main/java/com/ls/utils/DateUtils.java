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
		String timeZone = PreferencesManager.getInstance().getTimeZone();
		SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
		format.setTimeZone(TimeZone.getTimeZone(timeZone));

		Date date = null;
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (date != null) {
			SimpleDateFormat newFormat = new SimpleDateFormat("kk:mm");
			newFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

			return newFormat.format(date.getTime());
		} else {
			return strDate;
		}
	}

	@Nullable
	public static Date convertTime(String strDate) {
		String timeZone = PreferencesManager.getInstance().getTimeZone();
		Date date;

		if (strDate.toLowerCase().contains("pm") || strDate.toLowerCase().contains("am")) {
			SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
			format.setTimeZone(TimeZone.getTimeZone(timeZone));

			try {
				date = format.parse(strDate);
				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			SimpleDateFormat format = new SimpleDateFormat("kk:mm");
			format.setTimeZone(TimeZone.getTimeZone(timeZone));
			try {
				date = format.parse(strDate);

				return date;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Nullable
	public static Date convertDate(String day) {
		String timeZone = PreferencesManager.getInstance().getTimeZone();
		SimpleDateFormat format = new SimpleDateFormat("d-MM-yyyy");
		format.setTimeZone(TimeZone.getTimeZone(timeZone));

		try {
			return format.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getWeekDay(long millis) {
		SimpleDateFormat format = new SimpleDateFormat("EEE");
		String timeZone = PreferencesManager.getInstance().getTimeZone();
		format.setTimeZone(TimeZone.getTimeZone(timeZone));

		return format.format(new Date(millis));
	}

    public static long convertWeekDayToLong(String weekDay) {
        SimpleDateFormat format = new SimpleDateFormat("EEE");
		String timeZone = PreferencesManager.getInstance().getTimeZone();
		format.setTimeZone(TimeZone.getTimeZone(timeZone));

        Date date;
        try {
            date = format.parse(weekDay);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
