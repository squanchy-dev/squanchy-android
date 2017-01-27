package com.connfa.schedule.domain;

import android.content.Context;

import com.connfa.utils.DateUtils;

import java.util.Date;

public class SchedulePage {

    private final Date date;           // TODO use JodaTime

    public SchedulePage(Date date) {
        this.date = date;
    }

    public String formattedTitle(Context context) {
        return DateUtils.getWeekNameAndDate(context, date.getTime());
    }
}
