package net.squanchy.navigation.deeplink

import android.content.Intent
import java.net.URI

internal class DeepLinkRouter(private val scheme: String, private val navigator: DeepLinkNavigator) {

    init {
        if ("[^\\w]".toRegex().containsMatchIn(scheme)) {
            throw IllegalArgumentException("The scheme must not contain non-word characters. Make sure you didn't include the '://' part.")
        }
    }

    fun navigateTo(url: String) {
        val uri = parseUri(url)
        if (!uri.scheme.equals(scheme, ignoreCase = true)) {
            throw RoutingException.unhandledScheme(url)
        }

        getNavigationFor(uri).invoke(navigator)
    }

    private fun parseUri(url: String): URI {
        try {
            return URI.create(url).normalize()
        } catch (e: IllegalArgumentException) {
            throw RoutingException.malformedUrl(url, e)
        }
    }

    fun hasDeepLink(intent: Intent): Boolean {
        val hasViewAction = Intent.ACTION_VIEW == intent.action
        val hasLauncherCategory = intent.hasCategory(Intent.CATEGORY_LAUNCHER)
        val hasData = intent.data != null
        return hasViewAction && !hasLauncherCategory && hasData
    }
}
