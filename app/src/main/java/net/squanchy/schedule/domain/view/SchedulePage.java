package net.squanchy.schedule.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@AutoValue
public abstract class SchedulePage {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final String TITLE_FORMAT_TEMPLATE = "EEEE d";

    public static SchedulePage create(String date, List<Event> events) {
        DateTime dateTime = DateTime.parse(date, DATE_FORMATTER);
        String title = dateTime.toString(TITLE_FORMAT_TEMPLATE);
        return new AutoValue_SchedulePage(title, events);
    }

    public abstract String title();

    public abstract List<Event> events();
}
