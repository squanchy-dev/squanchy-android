package net.squanchy.schedule.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

import org.joda.time.LocalDateTime;

@AutoValue
public abstract class SchedulePage {

    public static SchedulePage create(String dayId, LocalDateTime date, List<Event> events) {
        return new AutoValue_SchedulePage(dayId, date, events);
    }

    public abstract String dayId();

    public abstract LocalDateTime date();

    public abstract List<Event> events();
}
