package net.squanchy.speaker.domain.view;

import net.squanchy.support.lang.Optional;

public final class SpeakerFixtures {

    private String id = "bananana";
    private long numericId = 5466L;
    private String name = "Banana Joe";
    private String bio = "Joe ¡oh Banana Joe!\nTu tienes, ¡oh Banana nana Joe!\nCorazón gigante y alma soñante\n¡Oh Banana nana Joe!\nJoe ¡oh Banana Joe!\nTu eres, ¡oh Banana na na Joe!\nUn gran marinero con puños de acero";
    private Optional<String> photoUrl = Optional.of("https://i.ytimg.com/vi/-1HB26ko2H8/hqdefault.jpg");
    private Optional<String> companyName = Optional.of("Amantido");
    private Optional<String> companyUrl = Optional.absent();
    private Optional<String> personalUrl = Optional.of("https://en.wikipedia.org/wiki/Banana_Joe_(film)");
    private Optional<String> twitterUsername = Optional.of("@bananaJoe1982");

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

    public SpeakerFixtures withBio(String bio) {
        this.bio = bio;
        return this;
    }

    public SpeakerFixtures withCompanyName(Optional<String> companyName) {
        this.companyName = companyName;
        return this;
    }

    public SpeakerFixtures withCompanyUrl(Optional<String> companyUrl) {
        this.companyUrl = companyUrl;
        return this;
    }

    public SpeakerFixtures withPersonalUrl(Optional<String> personalUrl) {
        this.personalUrl = personalUrl;
        return this;
    }

    public SpeakerFixtures withPhotoUrl(Optional<String> photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public SpeakerFixtures withTwitterUsername(Optional<String> twitterUsername) {
        this.twitterUsername = twitterUsername;
        return this;
    }

    public Speaker build() {
        return Speaker.create(
                id,
                numericId,
                name,
                bio,
                companyName,
                companyUrl,
                personalUrl,
                photoUrl,
                twitterUsername
        );
    }
}
