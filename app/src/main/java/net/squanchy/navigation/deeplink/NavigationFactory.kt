package net.squanchy.navigation.deeplink

import java.net.URI
import java.util.Locale

internal fun getNavigationFor(uri: URI): (DeepLinkNavigator) -> Unit {
    val host = uri.host ?: throw RoutingException.unhandledPath(uri.toString())

    when (host.toLowerCase(Locale.US)) {
        "schedule" -> return { it.toSchedule(uri.path) }
        "event" -> return { it.toEventDetails(uri.path) }
        "speaker" -> return { it.toSpeakerUri(uri.path) }
        "favorites" -> return { it.toFavorites() }
        "social" -> return { it.toSocial() }
        "venue" -> return { it.toVenue() }
        "debug" -> return { it.toDebug() }
        else -> throw RoutingException.unhandledPath(uri.toString())
    }
}
