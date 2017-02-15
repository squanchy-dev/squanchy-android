package net.squanchy.schedule.domain.view;

import android.view.View;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.support.view.Visibility;

@AutoValue
public abstract class Event {

    public static Event create(
            long eventId,
            int dayId,
            String title,
            String place,
            ExperienceLevel experienceLevel,
            List<String> speakerNames
    ) {
        return new AutoValue_Event.Builder()
                .id(eventId)
                .day(dayId)
                .title(title)
                .place(place)
                .placeVisibility(place.isEmpty() ? View.GONE : View.VISIBLE)
                .speakersNames(speakersNamesStringFrom(speakerNames))
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

    public abstract long id();

    public abstract String title();

    public abstract String place();

    @Visibility
    public abstract int placeVisibility();

    @Visibility
    public abstract int trackVisibility();

    public abstract String speakersNames();

    @Visibility
    public abstract int speakersVisibility();

    public abstract ExperienceLevel experienceLevel();

    public abstract int day();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder day(int day);

        public abstract Builder title(String title);

        public abstract Builder place(String place);

        public abstract Builder placeVisibility(@Visibility int placeVisibility);

        public abstract Builder trackVisibility(@Visibility int trackVisibility);

        public abstract Builder speakersNames(String speakersNames);

        public abstract Builder speakersVisibility(@Visibility int speakersVisibility);

        public abstract Builder experienceLevel(ExperienceLevel experienceLevel);

        public abstract Event build();
    }
}
