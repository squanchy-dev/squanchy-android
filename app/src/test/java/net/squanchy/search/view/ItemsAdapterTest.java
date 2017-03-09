package net.squanchy.search.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.search.SearchResults;
import net.squanchy.speaker.domain.view.Speaker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static net.squanchy.schedule.domain.view.EventFixtures.anEvent;
import static net.squanchy.speaker.domain.view.SpeakerFixtures.aSpeaker;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class ItemsAdapterTest {

    private static final List<Event> NO_EVENTS = Collections.emptyList();
    private static final List<Speaker> NO_SPEAKERS = Collections.emptyList();

    private static final List<Speaker> ANY_TWO_SPEAKERS = Arrays.asList(
            aSpeaker().withId("banana").withNumericId(1).withName("Banana Joe").build(),
            aSpeaker().withId("potato").withNumericId(2).withName("Detective Patatorfio").build()
    );
    private static final List<Event> ANY_THREE_EVENTS = Arrays.asList(
            anEvent().withId("carrot").withNumericId(3).withTitle("Karotoff").build(),
            anEvent().withId("johnnyfer").withNumericId(4).withTitle("Johnnyfer Jaypegg").build(),
            anEvent().withId("bau").withNumericId(5).withTitle("Cane AAAAAAH").build()
    );

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SearchResults searchResults;

    private ItemsAdapter itemsAdapter;

    @Before
    public void setUp() {
        itemsAdapter = new ItemsAdapter(searchResults);
    }

    @Test
    public void givenEmptySearchResults_whenGettingTotalItemsCount_thenReturnsZero() {
        givenEmptySearchResults();

        int totalItemsCount = itemsAdapter.totalItemsCount();

        assertThat(totalItemsCount).isZero();
    }

    @Test
    public void givenSearchResultsWithOnlyEvents_whenGettingTotalItemsCount_thenReturnsOneMoreThanTheNumberOfSpeakers() {
        givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

        int totalItemsCount = itemsAdapter.totalItemsCount();

        assertThat(totalItemsCount).isEqualTo(4);
    }

    @Test
    public void givenSearchResultsWithOnlySpeakers_whenGettingTotalItemsCount_thenReturnsOneMoreThanTheNumberOfSpeakers() {
        givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

        int totalItemsCount = itemsAdapter.totalItemsCount();

        assertThat(totalItemsCount).isEqualTo(3);
    }

    @Test
    public void givenSearchResultsWithEventsAndSpeakers_whenGettingTotalItemsCount_thenReturnsTwoMoreThanTheNumberOfEventsAndSpeakers() {
        givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

        int totalItemsCount = itemsAdapter.totalItemsCount();

        assertThat(totalItemsCount).isEqualTo(7);
    }

    private void givenEmptySearchResults() {
        givenSearchResultsWith(NO_EVENTS, NO_SPEAKERS);
    }

    private void givenSearchResultsWith(List<Event> events, List<Speaker> speakers) {
        given(searchResults.events()).willReturn(events);
        given(searchResults.speakers()).willReturn(speakers);
        given(searchResults.isEmpty()).willReturn(events.isEmpty() && speakers.isEmpty());
    }
}
