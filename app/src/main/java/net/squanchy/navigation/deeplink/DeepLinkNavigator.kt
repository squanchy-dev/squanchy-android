package net.squanchy.navigation.deeplink

import net.squanchy.navigation.Navigator

internal class DeepLinkNavigator(private val navigator: Navigator) {

    fun toSchedule(path: String) {
        val pathSegments = extractSegments(path)

        val dayId = if (pathSegments.isNotEmpty()) pathSegments.first() else null
        val eventId = if (pathSegments.size > 1) pathSegments[1] else null

        navigator.toSchedule(dayId, eventId)
    }

    fun toEventDetails(path: String) {
        val pathSegments = extractSegments(path)
        if (pathSegments.isEmpty()) {
            throw DeepLinkParsingException.eventDetailsMissingEventId(path)
        }

        val eventId = pathSegments.first()
        navigator.toEventDetails(eventId)
    }

    fun toSpeakerUri(path: String) {
        val pathSegments = extractSegments(path)
        if (pathSegments.isEmpty()) {
            throw DeepLinkParsingException.speakerDetailsMissingSpeakerId(path)
        }

        val speakerId = pathSegments.first()
        navigator.toSpeakerDetails(speakerId)
    }

    private fun extractSegments(path: String): List<String> {
        return path.split("/".toRegex())
            .filterNot { it.isEmpty() }
    }

    fun toFavorites() {
        navigator.toFavorites()
    }

    fun toSocial() {
        navigator.toTwitterFeed()
    }

    fun toVenue() {
        navigator.toVenueInfo()
    }

    fun toDebug() {
        navigator.toDebugSettings()
    }
}
