package net.squanchy.navigation.deeplink;

import net.squanchy.navigation.Navigator;
import net.squanchy.support.lang.Optional;

class DeepLinkNavigator {

    private final Navigator navigator;

    DeepLinkNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    void toSchedule(String path) {
        String[] pathSegments = path.split("/");

        Optional<String> dayId = Optional.absent();
        if (pathSegments.length > 0) {
            dayId = Optional.of(pathSegments[0]);
        }

        Optional<String> eventId = Optional.absent();
        if (pathSegments.length > 1) {
            eventId = Optional.of(pathSegments[1]);
        }
        navigator.toSchedule(dayId, eventId);
    }

    void toEventDetails(String path) {
        String[] pathSegments = path.split("/");

        if (pathSegments.length > 0) {
            String eventId = pathSegments[0];
            navigator.toEventDetails(eventId);
        } else {
            throw DeepLinkParsingException.eventDetailsMissingEventId(path);
        }
    }

    void toSpeakerUri(String path) {
        String[] pathSegments = path.split("/");

        if (pathSegments.length > 0) {
            String speakerId = pathSegments[0];
            navigator.toSpeakerDetails(speakerId);
        } else {
            throw DeepLinkParsingException.speakerDetailsMissingSpeakerId(path);
        }
    }

    void toFavorites() {
        navigator.toFavorites();
    }

    void toSocial() {
        navigator.toTwitterFeed();
    }

    void toVenue() {
        navigator.toVenueInfo();
    }

    void toDebug() {
        navigator.toDebugSettings();
    }
}
