package net.squanchy.schedule.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Schedule {

    public static Schedule create(List<SchedulePage> pages) {
        return new AutoValue_Schedule(pages);
    }

    public abstract List<SchedulePage> pages();

    public boolean hasPages() {
        return !pages().isEmpty();
    }
}
