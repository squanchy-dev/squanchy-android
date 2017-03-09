package net.squanchy.search.engines;

import net.squanchy.speaker.domain.view.Speaker;

import org.junit.Test;

import static net.squanchy.speaker.domain.view.SpeakerFixtures.aSpeaker;
import static org.fest.assertions.api.Assertions.assertThat;

public class SpeakerSearchEngineTest {

    private static final Speaker ANY_SPEAKER = aSpeaker().build();

    private static final Speaker MATCHING_SPEAKER = aSpeaker()
            .withName("Banana Joe")
            .build();

    private final SpeakerSearchEngine searchEngine = new SpeakerSearchEngine();

    @Test
    public void givenEmptyQuery_whenMatching_thenReturnsFalse() {
        String query = "";

        boolean matches = searchEngine.matches(ANY_SPEAKER, query);

        assertThat(matches).isFalse();
    }

    @Test
    public void givenQueryShorterThanTwoCharacters_whenMatching_thenReturnsFalse() {
        String query = "a";

        boolean matches = searchEngine.matches(MATCHING_SPEAKER, query);

        assertThat(matches).isFalse();
    }

    @Test
    public void givenOfAtLeastTwoCharacters_whenMatching_thenReturnsTrue() {
        String query = "na";

        boolean matches = searchEngine.matches(MATCHING_SPEAKER, query);

        assertThat(matches).isTrue();
    }

    @Test
    public void givenSpeakerWithNameMatchingQueryExactly_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_SPEAKER, "Banana Joe");

        assertThat(matches).isTrue();
    }

    @Test
    public void givenSpeakerWithNameContainingQuery_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_SPEAKER, "anana J");

        assertThat(matches).isTrue();
    }

    @Test
    public void givenSpeakerWithNameMatching_butWithDifferentCasing_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_SPEAKER, "anana J");

        assertThat(matches).isTrue();
    }

    @Test
    public void givenSpeakerWithNameMatchingQueryExactly_butWithDiacritics_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_SPEAKER, "anÀna J");

        assertThat(matches).isTrue();
    }
}
