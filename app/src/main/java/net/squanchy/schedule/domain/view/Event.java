package net.squanchy.schedule.domain.view;

import android.support.annotation.DrawableRes;
import android.view.View;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.R;
import net.squanchy.support.view.Visibility;

@AutoValue
public abstract class Event {

    public static Event create(
            long eventId,
            int dayId,
            String title,
            String place,
            int experienceLevel,
            List<String> speakers
    ) {
        return new AutoValue_Event.Builder()
                .id(eventId)
                .day(dayId)
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
    private static final int BEGINNER = 1;
    private static final int INTERMEDIATE = 2;
    private static final int ADVANCED = 3;

    @DrawableRes
    private static int experienceLevelIconFor(int level) {
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

    public abstract int day();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder day(int day);

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
