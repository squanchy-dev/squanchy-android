package net.squanchy.navigation.deeplink

internal class DeepLinkParsingException private constructor(message: String) : RuntimeException(message) {

    companion object {

        fun eventDetailsMissingEventId(path: String): DeepLinkParsingException {
            return DeepLinkParsingException("Event details deeplink is missing event ID: \"$path\"")
        }

        fun speakerDetailsMissingSpeakerId(path: String): DeepLinkParsingException {
            return DeepLinkParsingException("Speaker details deeplink is missing speaker ID: \"$path\"")
        }
    }
}
