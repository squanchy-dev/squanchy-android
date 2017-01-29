package com.connfa.schedule.domain.view;

import android.support.annotation.DrawableRes;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Event {

    public static Builder builder() {
        return new AutoValue_Event.Builder();
    }

    public abstract long id();

    public abstract String title();

    public abstract String place();

    public abstract int placeVisibility();

    public abstract int trackVisibility();

    public abstract String speakers();

    public abstract int speakersVisibility();

    @DrawableRes
    public abstract int experienceLevelIcon();

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder id(long id);

        public abstract Builder title(String title);

        public abstract Builder place(String place);

        public abstract Builder placeVisibility(int placeVisibility);

        public abstract Builder trackVisibility(int trackVisibility);

        public abstract Builder speakers(String speakers);

        public abstract Builder speakersVisibility(int speakersVisibility);

        public abstract Builder experienceLevelIcon(@DrawableRes int experienceLevelIcon);

        public abstract Event build();
    }

}
