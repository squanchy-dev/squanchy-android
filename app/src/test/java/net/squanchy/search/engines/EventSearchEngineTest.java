package net.squanchy.search.engines;

import net.squanchy.schedule.domain.view.Event;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static net.squanchy.schedule.domain.view.EventFixtures.anEvent;

public class EventSearchEngineTest {

    private static final Event ANY_EVENT = anEvent().build();

    private static final Event MATCHING_EVENT = anEvent()
            .withTitle("Banane")
            .withType(Event.Type.TALK)
            .build();

    private final EventSearchEngine searchEngine = new EventSearchEngine();

    @Test
    public void givenEmptyQuery_whenMatching_thenReturnsFalse() {
        String query = "";

        boolean matches = searchEngine.matches(ANY_EVENT, query);

        assertThat(matches).isFalse();
    }

    @Test
    public void givenQueryShorterThanTwoCharacters_whenMatching_thenReturnsFalse() {
        String query = "e";

        boolean matches = searchEngine.matches(MATCHING_EVENT, query);

        assertThat(matches).isFalse();
    }

    @Test
    public void givenOfAtLeastTwoCharacters_whenMatching_thenReturnsTrue() {
        String query = "ne";

        boolean matches = searchEngine.matches(MATCHING_EVENT, query);

        assertThat(matches).isTrue();
    }

    @Test
    public void givenEventWithCoffeeBreakType_whenMatching_thenReturnsFalse() {
        Event event = anEvent().withTitle("anything").withType(Event.Type.COFFEE_BREAK).build();

        boolean matches = searchEngine.matches(event, "anything");

        assertThat(matches).isFalse();
    }

    @Test
    public void givenEventWithLunchType_whenMatching_thenReturnsFalse() {
        Event event = anEvent().withTitle("anything").withType(Event.Type.LUNCH).build();

        boolean matches = searchEngine.matches(event, "anything");

        assertThat(matches).isFalse();
    }

    @Test
    public void givenEventWithRegistrationType_whenMatching_thenReturnsFalse() {
        Event event = anEvent().withTitle("anything").withType(Event.Type.REGISTRATION).build();

        boolean matches = searchEngine.matches(event, "anything");

        assertThat(matches).isFalse();
    }

    @Test
    public void givenEventWithSocialType_whenMatching_thenReturnsFalse() {
        Event event = anEvent().withTitle("anything").withType(Event.Type.SOCIAL).build();

        boolean matches = searchEngine.matches(event, "anything");

        assertThat(matches).isFalse();
    }

    @Test
    public void givenEventWithOtherType_whenMatching_thenReturnsFalse() {
        Event event = anEvent().withTitle("anything").withType(Event.Type.OTHER).build();

        boolean matches = searchEngine.matches(event, "anything");

        assertThat(matches).isFalse();
    }

    @Test
    public void givenEventWithTitleMatchingQueryExactly_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_EVENT, "Banane");

        assertThat(matches).isTrue();
    }

    @Test
    public void givenEventWithTitleMatchingQueryExactly_butWithDifferentCasing_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_EVENT, "banAne");

        assertThat(matches).isTrue();
    }

    @Test
    public void givenEventWithTitleContainingQuery_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_EVENT, "Banane");

        assertThat(matches).isTrue();
    }

    @Test
    public void givenEventWithTitleMatchingQueryExactly_butWithDiacritics_whenMatching_thenReturnsTrue() {

        boolean matches = searchEngine.matches(MATCHING_EVENT, "BanÀne");

        assertThat(matches).isTrue();
    }
}
