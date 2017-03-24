package net.squanchy.navigation.deeplink;

import java.net.URI;
import java.util.regex.Pattern;

import net.squanchy.navigation.Navigator;

public class DeepLinkRouter {

    private static final Pattern PATTERN_VALID_SCHEME = Pattern.compile("[^\\w]");

    private final String scheme;
    private final NavigationStrategyFactory navigationStrategyFactory;
    private final Navigator navigator;

    DeepLinkRouter(String scheme, NavigationStrategyFactory navigationStrategyFactory, Navigator navigator) {
        this.navigator = navigator;
        validateScheme(scheme);
        this.scheme = scheme;
        this.navigationStrategyFactory = navigationStrategyFactory;
    }

    private void validateScheme(String scheme) {
        if (PATTERN_VALID_SCHEME.matcher(scheme).find()) {
            throw new IllegalArgumentException("The scheme must not contain non-word characters. Make sure you didn't include the '://' part.");
        }
    }

    public void navigateTo(String url) {
        URI uri = parseUri(url);
        if (!uri.getScheme().equalsIgnoreCase(scheme)) {
            throw RoutingException.unhandledScheme(url);
        }

        NavigationStrategy navigationStrategy = navigationStrategyFactory.getNavigationStrategyFor(uri);
        navigationStrategy.navigate(navigator);
    }

    private URI parseUri(String url) {
        try {
            return URI.create(url)
                    .normalize();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw RoutingException.malformedUrl(url, e);
        }
    }
}
