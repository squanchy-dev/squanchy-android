package net.squanchy.navigation.deeplink;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.navigation.Navigator;
import net.squanchy.support.lang.Optional;

class DeepLinkNavigator {

    private final Navigator navigator;

    DeepLinkNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    void toSchedule(String path) {
        List<String> pathSegments = extractSegments(path);

        Optional<String> dayId = Optional.absent();
        if (!pathSegments.isEmpty()) {
            dayId = Optional.of(pathSegments.get(0));
        }

        Optional<String> eventId = Optional.absent();
        if (pathSegments.size() > 1) {
            eventId = Optional.of(pathSegments.get(1));
        }
        navigator.toSchedule(dayId, eventId);
    }

    void toEventDetails(String path) {
        List<String> pathSegments = extractSegments(path);
        if (pathSegments.isEmpty()) {
            throw DeepLinkParsingException.eventDetailsMissingEventId(path);
        }

        String eventId = pathSegments.get(0);
        navigator.toEventDetails(eventId);
    }

    void toSpeakerUri(String path) {
        List<String> pathSegments = extractSegments(path);
        if (pathSegments.isEmpty()) {
            throw DeepLinkParsingException.speakerDetailsMissingSpeakerId(path);
        }

        String speakerId = pathSegments.get(0);
        navigator.toSpeakerDetails(speakerId);
    }

    private List<String> extractSegments(String path) {
        String[] allSegments = path.split("/");
        ArrayList<String> filteredSegments = new ArrayList<>();
        for (String segment : allSegments) {
            if (!segment.isEmpty()) {
                filteredSegments.add(segment);
            }
        }
        return filteredSegments;
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
