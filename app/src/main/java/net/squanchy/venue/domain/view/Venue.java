package net.squanchy.venue.domain.view;

import com.google.auto.value.AutoValue;

import org.joda.time.DateTimeZone;

@AutoValue
public abstract class Venue {

    public static Venue create(String name, String address, double latitude, double longitude, String description, String mapUrl, String timezoneId) {
        return new AutoValue_Venue.Builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .description(description)
                .mapUrl(mapUrl)
                .timeZone(DateTimeZone.forID(timezoneId))
                .build();
    }

    public abstract String name();

    public abstract String address();

    public abstract double latitude();

    public abstract double longitude();

    public abstract String description();

    public abstract String mapUrl();

    public abstract DateTimeZone timeZone();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder name(String name);

        public abstract Builder address(String address);

        public abstract Builder latitude(double latitude);

        public abstract Builder longitude(double longitude);

        public abstract Builder description(String description);

        public abstract Builder mapUrl(String mapUrl);

        public abstract Builder timeZone(DateTimeZone timeZone);

        public abstract Venue build();
    }
}
