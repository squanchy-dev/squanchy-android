package net.squanchy.schedule.domain.view;

import android.view.View;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.view.Visibility;

@AutoValue
public abstract class Event {

    public static Event create(
            String eventId,
            long numericEventId,
            String dayId,
            String title,
            String place,
            Optional<ExperienceLevel> experienceLevel,
            List<String> speakerNames
    ) {
        return new AutoValue_Event.Builder()
                .id(eventId)
                .numericId(numericEventId)
                .dayId(dayId)
                .title(title)
                .place(Optional.fromNullable(place))
                .placeVisibility(place.isEmpty() ? View.GONE : View.VISIBLE)
                .speakersNames(Optional.fromNullable(speakersNamesStringFrom(speakerNames)))
                .speakersVisibility(speakerNames.isEmpty() ? View.GONE : View.VISIBLE)
                .trackVisibility(View.GONE) // todo add track
                .experienceLevel(experienceLevel)
                .build();
    }

    private static String speakersNamesStringFrom(List<String> speakers) {
        StringBuilder speakersBuilder = new StringBuilder();
        for (String speaker : speakers) {
            if (speakersBuilder.length() > 0) {
                speakersBuilder.append(", ");
            }

            speakersBuilder.append(speaker);
        }

        return speakersBuilder.toString();
    }

    public abstract String id();

    public abstract long numericId();

    public abstract String title();

    public abstract Optional<String> place();

    @Visibility
    public abstract int placeVisibility();

    @Visibility
    public abstract int trackVisibility();

    public abstract Optional<String> speakersNames();

    @Visibility
    public abstract int speakersVisibility();

    public abstract Optional<ExperienceLevel> experienceLevel();

    public abstract String dayId();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(String id);

        public abstract Builder numericId(long id);

        public abstract Builder dayId(String dayId);

        public abstract Builder title(String title);

        public abstract Builder place(Optional<String> place);

        public abstract Builder placeVisibility(@Visibility int placeVisibility);

        public abstract Builder trackVisibility(@Visibility int trackVisibility);

        public abstract Builder speakersNames(Optional<String> speakersNames);

        public abstract Builder speakersVisibility(@Visibility int speakersVisibility);

        public abstract Builder experienceLevel(Optional<ExperienceLevel> experienceLevel);

        public abstract Event build();
    }
}
