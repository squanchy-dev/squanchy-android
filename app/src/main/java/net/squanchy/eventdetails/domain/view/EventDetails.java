package net.squanchy.eventdetails.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.speaker.domain.view.Speaker;

@AutoValue
public abstract class EventDetails {

    public static EventDetails create(
            Event event,
            List<Speaker> speakers
    ) {
        return new AutoValue_EventDetails.Builder()
                .id(event.id())
                .event(event)
                .speakers(speakers)
                .build();
    }

    public abstract long id();

    public abstract Event event();

    public abstract List<Speaker> speakers();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder event(Event event);

        public abstract Builder speakers(List<Speaker> speakers);

        public abstract EventDetails build();
    }
}
