package net.squanchy.navigation.deeplink;

public class RoutingException extends RuntimeException {

    static RoutingException malformedUrl(String url, Throwable cause) {
        return new RoutingException("Malformed URL: \"" + url + "\"", cause);
    }

    static RoutingException unhandledScheme(String url) {
        return new RoutingException("The URL \"" + url + "\" doesn't have a valid scheme");
    }

    static RoutingException unhandledPath(String url) {
        return new RoutingException("The URL \"" + url + "\" doesn't match any valid link target");
    }

    private RoutingException(String message) {
        super(message);
    }

    private RoutingException(String message, Throwable cause) {
        super(message, cause);
    }
}
