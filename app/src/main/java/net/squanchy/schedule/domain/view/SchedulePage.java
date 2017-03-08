package net.squanchy.schedule.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

import org.joda.time.LocalDateTime;

@AutoValue
public abstract class SchedulePage {

    public static SchedulePage create(LocalDateTime date, List<Event> events) {
        return new AutoValue_SchedulePage(date, events);
    }

    public abstract LocalDateTime date();

    public abstract List<Event> events();
}
