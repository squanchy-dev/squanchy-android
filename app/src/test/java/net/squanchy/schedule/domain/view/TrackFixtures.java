package net.squanchy.schedule.domain.view;

import net.squanchy.support.lang.Optional;

final class TrackFixtures {

    private String id = "batracknana";
    private String name = "Bananas";
    private Optional<String> accentColor = Optional.of("#C0C0C0");
    private Optional<String> textColor = Optional.of("#FFFFFFFF");
    private Optional<String> iconUrl = Optional.of("http://squanchy.net/tracks/batracknana.webp");

    static TrackFixtures aTrack() {
        return new TrackFixtures();
    }

    private TrackFixtures() {
        // Not instantiable
    }

    public TrackFixtures withId(String id) {
        this.id = id;
        return this;
    }

    public TrackFixtures withName(String name) {
        this.name = name;
        return this;
    }

    public TrackFixtures withAccentColor(Optional<String> accentColor) {
        this.accentColor = accentColor;
        return this;
    }

    public TrackFixtures withTextColor(Optional<String> textColor) {
        this.textColor = textColor;
        return this;
    }

    public TrackFixtures withIconUrl(Optional<String> iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    Track build() {
        return Track.Companion.create(id, name, accentColor, textColor, iconUrl);
    }
}
