package net.squanchy.schedule.domain.view;

import com.google.auto.value.AutoValue;

import net.squanchy.support.lang.Optional;

@AutoValue
public abstract class Track {

    public static Track create(String id, String name, Optional<String> accentColor, Optional<String> textColor, Optional<String> iconUrl) {
        return new AutoValue_Track.Builder()
                .id(id)
                .name(name)
                .accentColor(accentColor)
                .textColor(textColor)
                .iconUrl(iconUrl)
                .build();
    }

    public abstract String id();

    public abstract String name();

    public abstract Optional<String> accentColor();

    public abstract Optional<String> textColor();

    public abstract Optional<String> iconUrl();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder accentColor(Optional<String> accentColor);

        public abstract Builder textColor(Optional<String> textColor);

        public abstract Builder iconUrl(Optional<String> iconUrl);

        public abstract Track build();
    }
}
