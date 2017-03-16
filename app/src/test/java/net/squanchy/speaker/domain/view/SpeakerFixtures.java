package net.squanchy.speaker.domain.view;

public final class SpeakerFixtures {

    private String id = "bananana";
    private long numericId = 5466L;
    private String name = "Banana Joe";
    private String photoUrl = "http://example.com/photos/banana.jpg";

    public static SpeakerFixtures aSpeaker() {
        return new SpeakerFixtures();
    }

    private SpeakerFixtures() {
        // Not instantiable
    }

    public SpeakerFixtures withId(String id) {
        this.id = id;
        return this;
    }

    public SpeakerFixtures withNumericId(long numericId) {
        this.numericId = numericId;
        return this;
    }

    public SpeakerFixtures withName(String name) {
        this.name = name;
        return this;
    }

    public SpeakerFixtures withPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public Speaker build() {
        return Speaker.create(id, numericId, name, photoUrl);
    }
}
