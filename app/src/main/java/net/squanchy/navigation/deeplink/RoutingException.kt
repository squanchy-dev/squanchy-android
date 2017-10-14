package net.squanchy.navigation.deeplink

internal class RoutingException : RuntimeException {

    private constructor(message: String) : super(message)

    private constructor(message: String, cause: Throwable) : super(message, cause)

    companion object {

        fun malformedUrl(url: String, cause: Throwable): RoutingException {
            return RoutingException("Malformed URL: \"$url\"", cause)
        }

        fun unhandledScheme(url: String): RoutingException {
            return RoutingException("The URL \"$url\" doesn't have a valid scheme")
        }

        fun unhandledPath(url: String): RoutingException {
            return RoutingException("The URL \"$url\" doesn't match any valid link target")
        }
    }
}
