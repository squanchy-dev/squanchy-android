package net.squanchy.schedule.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;

import org.joda.time.LocalDateTime;

@AutoValue
public abstract class Event {

    public static Event create(
            String eventId,
            long numericEventId,
            String dayId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String title,
            Optional<Place> place,
            Optional<ExperienceLevel> experienceLevel,
            List<Speaker> speakers,
            Type type,
            boolean favorited,
            String description,
            Optional<Track> track) {
        return new AutoValue_Event.Builder()
                .id(eventId)
                .numericId(numericEventId)
                .dayId(dayId)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .place(place)
                .speakers(speakers)
                .track(track)
                .experienceLevel(experienceLevel)
                .type(type)
                .favorited(favorited)
                .description(description)
                .build();
    }

    public abstract String id();

    public abstract long numericId();

    public abstract LocalDateTime startTime();

    public abstract LocalDateTime endTime();

    public abstract String title();

    public abstract Optional<Place> place();

    public abstract Optional<Track> track();

    public abstract List<Speaker> speakers();

    public abstract Optional<ExperienceLevel> experienceLevel();

    public abstract String dayId();

    public abstract Type type();

    public abstract boolean favorited();

    public abstract String description();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(String id);

        public abstract Builder numericId(long id);

        public abstract Builder dayId(String dayId);

        public abstract Builder startTime(LocalDateTime startTime);

        public abstract Builder endTime(LocalDateTime endTime);

        public abstract Builder title(String title);

        public abstract Builder place(Optional<Place> place);

        public abstract Builder track(Optional<Track> track);

        public abstract Builder speakers(List<Speaker> speakers);

        public abstract Builder experienceLevel(Optional<ExperienceLevel> experienceLevel);

        public abstract Builder type(Type type);

        public abstract Builder favorited(boolean favorited);

        public abstract Builder description(String description);

        public abstract Event build();
    }

    public enum Type {
        REGISTRATION("registration"),
        TALK("talk"),
        KEYNOTE("keynote"),
        COFFEE_BREAK("coffee_break"),
        LUNCH("lunch"),
        SOCIAL("social"),
        OTHER("other");

        private final String rawType;

        Type(String rawType) {
            this.rawType = rawType;
        }

        public static Type fromRawType(String rawType) {
            for (Type type : values()) {
                if (type.rawType.equalsIgnoreCase(rawType)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unsupported raw event type: " + rawType);
        }
    }
}
