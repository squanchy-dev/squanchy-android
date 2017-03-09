package net.squanchy.schedule.domain.view;

import java.util.Collections;
import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;

import org.joda.time.LocalDateTime;

public final class EventFixtures {

    private String eventId = "banana";
    private long numericEventId = 1234;
    private String dayId = "potato";
    private LocalDateTime startTime = new LocalDateTime(123456);
    private LocalDateTime endTime = new LocalDateTime(123666);
    private String title = "Hello \uD83C\uDF4C";    // Yes, that's a banana emoji. You never know
    private String place = null;        // TODO this should be Optional<Place>
    private Optional<ExperienceLevel> experienceLevel = Optional.absent();
    private List<Speaker> speakers = Collections.emptyList();
    private Event.Type type = Event.Type.OTHER;

    public static EventFixtures anEvent() {
        return new EventFixtures();
    }

    private EventFixtures() {
        // Not instantiable
    }

    public EventFixtures withEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public EventFixtures withNumericEventId(long numericEventId) {
        this.numericEventId = numericEventId;
        return this;
    }

    public EventFixtures withDayId(String dayId) {
        this.dayId = dayId;
        return this;
    }

    public EventFixtures withStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public EventFixtures withEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public EventFixtures withTitle(String title) {
        this.title = title;
        return this;
    }

    public EventFixtures withPlace(String place) {
        this.place = place;
        return this;
    }

    public EventFixtures withExperienceLevel(Optional<ExperienceLevel> experienceLevel) {
        this.experienceLevel = experienceLevel;
        return this;
    }

    public EventFixtures withSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
        return this;
    }

    public EventFixtures withType(Event.Type type) {
        this.type = type;
        return this;
    }

    public Event build() {
        return Event.create(
                eventId,
                numericEventId,
                dayId,
                startTime,
                endTime,
                title,
                place,
                experienceLevel,
                speakers,
                type
        );
    }
}
