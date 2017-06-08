package net.squanchy.navigation.deeplink;

final class DeepLinkParsingException extends RuntimeException {

    static DeepLinkParsingException eventDetailsMissingEventId(String path) {
        return new DeepLinkParsingException("Event details deeplink is missing event ID: \"" + path + "\"");
    }

    static DeepLinkParsingException speakerDetailsMissingSpeakerId(String path) {
        return new DeepLinkParsingException("Speaker details deeplink is missing speaker ID: \"" + path + "\"");
    }

    private DeepLinkParsingException(String message) {
        super(message);
    }
}
