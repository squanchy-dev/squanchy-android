package net.squanchy.schedule.domain.view;

import android.view.View;

import com.google.auto.value.AutoValue;

import java.util.Date;
import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.view.Visibility;

@AutoValue
public abstract class Event {

    public static Event create(
            long eventId,
            int dayId,
            Date start,
            Date end,
            String title,
            String place,
            ExperienceLevel experienceLevel,
            List<Speaker> speakers
    ) {
        return new AutoValue_Event.Builder()
                .id(eventId)
                .day(dayId)
                .start(start)
                .end(end)
                .title(title)
                .place(place)
                .placeVisibility(place.isEmpty() ? View.GONE : View.VISIBLE)
                .speakers(speakers)
                .speakersVisibility(speakers.isEmpty() ? View.GONE : View.VISIBLE)
                .trackVisibility(View.GONE) // todo add track
                .experienceLevel(experienceLevel)
                .build();
    }

    public abstract long id();

    public abstract String title();

    public abstract String place();

    @Visibility
    public abstract int placeVisibility();

    @Visibility
    public abstract int trackVisibility();

    public abstract List<Speaker> speakers();

    @Visibility
    public abstract int speakersVisibility();

    public abstract ExperienceLevel experienceLevel();

    public abstract int day();

    public abstract Date start();

    public abstract Date end();

    public String speakersNames() {
        StringBuilder speakersBuilder = new StringBuilder();
        for (Speaker speaker : speakers()) {
            if (speakersBuilder.length() > 0) {
                speakersBuilder.append(", ");
            }

            speakersBuilder.append(speaker.fullName());
        }

        return speakersBuilder.toString();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder day(int day);

        public abstract Builder start(Date start);

        public abstract Builder end(Date end);

        public abstract Builder title(String title);

        public abstract Builder place(String place);

        public abstract Builder placeVisibility(@Visibility int placeVisibility);

        public abstract Builder trackVisibility(@Visibility int trackVisibility);

        public abstract Builder speakers(List<Speaker> speakers);

        public abstract Builder speakersVisibility(@Visibility int speakersVisibility);

        public abstract Builder experienceLevel(ExperienceLevel experienceLevel);

        public abstract Event build();
    }
}
