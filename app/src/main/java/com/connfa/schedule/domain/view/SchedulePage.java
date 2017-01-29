package com.connfa.schedule.domain.view;

import android.content.Context;
import android.view.View;

import com.connfa.R;
import com.connfa.utils.DateUtils;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

@AutoValue
public abstract class SchedulePage {

    public static SchedulePage create(DateTime date) {
        return new AutoValue_SchedulePage(date, dummyEvents());
    }

    private static List<Event> dummyEvents() {
        Event event = Event.builder()
                .id(123456)
                .title("Test event \uD83C\uDF4C")
                .place("Rome, Italy")
                .placeVisibility(View.VISIBLE)
                .trackVisibility(View.GONE)
                .speakers("Carl Urbane, Lee Onwards")
                .speakersVisibility(View.VISIBLE)
                .experienceLevelIcon(R.drawable.ic_experience_intermediate)
                .build();

        return Collections.singletonList(event);
    }

    public abstract DateTime date();

    public abstract List<Event> events();

    public String formattedTitle(Context context) {
        return DateUtils.getWeekNameAndDate(context, date().getMillis());
    }
}
