package net.squanchy.schedule.domain.view;

import android.view.View;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.view.Visibility;

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
            String place,
            Optional<ExperienceLevel> experienceLevel,
            List<Speaker> speakers,
            Type type,
            boolean favorited
    ) {
        Optional<String> placeOptional = Optional.fromNullable(place);        // TODO get Optional<Place> as type in here
        return new AutoValue_Event.Builder()
                .id(eventId)
                .numericId(numericEventId)
                .dayId(dayId)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .place(placeOptional)
                .placeVisibility(placeOptional.isPresent() ? View.VISIBLE : View.GONE)
                .speakers(speakers)
                .speakersVisibility(speakers.isEmpty() ? View.GONE : View.VISIBLE)
                .trackVisibility(View.GONE)     // TODO add track
                .experienceLevel(experienceLevel)
                .type(type)
                .favorited(favorited)
                .build();
    }

    public abstract String id();

    public abstract long numericId();

    public abstract LocalDateTime startTime();

    public abstract LocalDateTime endTime();

    public abstract String title();

    public abstract Optional<String> place();

    @Visibility
    public abstract int placeVisibility();

    @Visibility
    public abstract int trackVisibility();

    public abstract List<Speaker> speakers();

    @Visibility
    public abstract int speakersVisibility();

    public abstract Optional<ExperienceLevel> experienceLevel();

    public abstract String dayId();

    public abstract Type type();

    public abstract boolean favorited();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(String id);

        public abstract Builder numericId(long id);

        public abstract Builder dayId(String dayId);

        public abstract Builder startTime(LocalDateTime startTime);

        public abstract Builder endTime(LocalDateTime endTime);

        public abstract Builder title(String title);

        public abstract Builder place(Optional<String> place);

        public abstract Builder placeVisibility(@Visibility int placeVisibility);

        public abstract Builder trackVisibility(@Visibility int trackVisibility);

        public abstract Builder speakers(List<Speaker> speakers);

        public abstract Builder speakersVisibility(@Visibility int speakersVisibility);

        public abstract Builder experienceLevel(Optional<ExperienceLevel> experienceLevel);

        public abstract Builder type(Type type);

        public abstract Builder favorited(boolean favorited);

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
