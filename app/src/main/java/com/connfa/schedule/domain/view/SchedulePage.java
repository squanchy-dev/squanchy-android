package com.connfa.schedule.domain.view;

import android.content.Context;

import com.connfa.utils.DateUtils;
import com.google.auto.value.AutoValue;

import java.util.List;

import org.joda.time.DateTime;

@AutoValue
public abstract class SchedulePage {

    // todo parse date, format it
    public static SchedulePage create(String date, List<Event> events) {
        return new AutoValue_SchedulePage(date, events);
    }

    public abstract String title();

    public abstract List<Event> events();

    private static String formattedTitle(Context context, DateTime date) {
        return DateUtils.getWeekNameAndDate(context, date.getMillis());
    }
}
