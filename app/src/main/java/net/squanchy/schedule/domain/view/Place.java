package net.squanchy.schedule.domain.view;

import com.google.auto.value.AutoValue;

import net.squanchy.support.lang.Optional;

@AutoValue
public abstract class Place {

    public static Place create(String id, String name, Optional<String> floor) {
        return new AutoValue_Place.Builder()
                .id(id)
                .name(name)
                .floor(floor)
                .build();
    }

    public abstract String id();

    public abstract String name();

    public abstract Optional<String> floor();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder floor(Optional<String> floor);

        public abstract Place build();
    }
}
