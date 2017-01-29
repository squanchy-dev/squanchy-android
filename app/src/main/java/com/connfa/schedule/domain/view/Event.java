package com.connfa.schedule.domain.view;

import android.support.annotation.DrawableRes;
import android.view.View;

import com.connfa.R;
import com.connfa.support.view.Visibility;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Event {

    public static Event create(
            long eventId,
            String title,
            String place,
            int experienceLevel,
            List<String> speakers
    ) {

        return builder()
                .id(eventId)
                .title(title)
                .place(place)
                .placeVisibility(place.isEmpty() ? View.GONE : View.VISIBLE)
                .speakers(speakerNames(speakers))
                .speakersVisibility(speakers.isEmpty() ? View.GONE : View.VISIBLE)
                .trackVisibility(View.GONE) // todo add track
                .experienceLevelIcon(experienceLevelIconFor(experienceLevel))
                .build();
    }

    private static String speakerNames(List<String> speakers) {
        StringBuilder speakersBuilder = new StringBuilder();
        for (String speaker : speakers) {
            if (speakersBuilder.length() > 0) {
                speakersBuilder.append(", ");
            }

            speakersBuilder.append(speaker);
        }

        return speakersBuilder.toString();
    }

    // TODO: move in its own enum
    public static final int BEGINNER = 1;
    public static final int INTERMEDIATE = 2;
    public static final int ADVANCED = 3;

    @DrawableRes
    public static int experienceLevelIconFor(int level) {
        switch (level) {
            case BEGINNER:
                return R.drawable.ic_experience_beginner;

            case INTERMEDIATE:
                return R.drawable.ic_experience_intermediate;

            case ADVANCED:
                return R.drawable.ic_experience_advanced;

            default:
                throw new IllegalArgumentException("Level " + level + " is invalid");
        }
    }

    public static Builder builder() {
        return new AutoValue_Event.Builder();
    }

    public abstract long id();

    public abstract String title();

    public abstract String place();

    @Visibility
    public abstract int placeVisibility();

    @Visibility
    public abstract int trackVisibility();

    public abstract String speakers();

    @Visibility
    public abstract int speakersVisibility();

    @DrawableRes
    public abstract int experienceLevelIcon();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder id(long id);

        public abstract Builder title(String title);

        public abstract Builder place(String place);

        public abstract Builder placeVisibility(@Visibility int placeVisibility);

        public abstract Builder trackVisibility(@Visibility int trackVisibility);

        public abstract Builder speakers(String speakers);

        public abstract Builder speakersVisibility(@Visibility int speakersVisibility);

        public abstract Builder experienceLevelIcon(@DrawableRes int experienceLevelIcon);

        public abstract Event build();
    }
}
