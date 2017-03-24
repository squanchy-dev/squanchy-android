package net.squanchy.navigation.deeplink;

import java.net.URI;
import java.util.Locale;

class NavigationStrategyFactory {

    private final DeepLinkNavigator navigator;

    NavigationStrategyFactory(DeepLinkNavigator navigator) {
        this.navigator = navigator;
    }

    NavigationStrategy getNavigationStrategyFor(URI uri) {
        String host = uri.getHost();
        if (host == null) {
            throw RoutingException.unhandledPath(uri.toString());
        }

        switch (host.toLowerCase(Locale.US)) {
            case "schedule":
                return scheduleUri -> navigator.toSchedule(uri.getPath());
            case "event":
                return eventUri -> navigator.toEventDetails(uri.getPath());
            case "speaker":
                return speakerUri -> navigator.toSpeakerUri(uri.getPath());
            case "favorites":
                return favoritesUri -> navigator.toFavorites();
            case "social":
                return socialUri -> navigator.toSocial();
            case "venue":
                return venueUri -> navigator.toVenue();
            case "debug":
                return debugUri -> navigator.toDebug();
            default:
                throw RoutingException.unhandledPath(uri.toString());
        }
    }
}
