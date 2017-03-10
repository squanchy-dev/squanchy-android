package net.squanchy.search.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.search.SearchResults;
import net.squanchy.search.view.SpeakerAdapter.ViewTypeId;
import net.squanchy.speaker.domain.view.Speaker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static net.squanchy.schedule.domain.view.EventFixtures.anEvent;
import static net.squanchy.speaker.domain.view.SpeakerFixtures.aSpeaker;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(Enclosed.class)
public class ItemsAdapterTest {

    public static class TotalItemsCount extends BaseTest {

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

            assertThat(totalItemsCount).isEqualTo(4);      // 4 = 1 header + 3 events
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingTotalItemsCount_thenReturnsOneMoreThanTheNumberOfSpeakers() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            int totalItemsCount = itemsAdapter.totalItemsCount();

            assertThat(totalItemsCount).isEqualTo(3);      // 3 = 1 header + 2 speakers
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingTotalItemsCount_thenReturnsTwoMoreThanTheNumberOfEventsAndSpeakers() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            int totalItemsCount = itemsAdapter.totalItemsCount();

            assertThat(totalItemsCount).isEqualTo(7);      // 7 = 1 header + 3 events + 1 header + 2 speakers
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAt_thenReturns() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            int totalItemsCount = itemsAdapter.totalItemsCount();

            assertThat(totalItemsCount).isEqualTo(7);
        }
    }

    public static class ViewType extends BaseTest {

        @Test
        public void givenEmptySearchResults_whenGettingViewTypeAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults();
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.viewTypeAtAbsolutePosition(0);
        }

        @Test
        public void givenAnySearchResults_whenGettingViewTypeAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.viewTypeAtAbsolutePosition(-1);
        }

        @Test
        public void givenAnySearchResults_whenGettingViewTypePositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.viewTypeAtAbsolutePosition(7);      // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingViewTypeAtEventsHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER);
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingViewTypeAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(1);      // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.EVENT);
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingViewTypeAtSpeakerHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER);
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingViewTypeAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(1);      // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.SPEAKER);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtEventsHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(1);      // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.EVENT);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtSpeakerHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(4);      // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            int viewType = itemsAdapter.viewTypeAtAbsolutePosition(5);     // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.SPEAKER);
        }
    }

    public abstract static class BaseTest {

        static final List<Event> NO_EVENTS = Collections.emptyList();
        static final List<Speaker> NO_SPEAKERS = Collections.emptyList();

        static final List<Speaker> ANY_TWO_SPEAKERS = Arrays.asList(
                aSpeaker().withId("banana").withNumericId(1).withName("Banana Joe").build(),
                aSpeaker().withId("potato").withNumericId(2).withName("Detective Patatorfio").build()
        );
        static final List<Event> ANY_THREE_EVENTS = Arrays.asList(
                anEvent().withId("carrot").withNumericId(3).withTitle("Karotoff").build(),
                anEvent().withId("johnnyfer").withNumericId(4).withTitle("Johnnyfer Jaypegg").build(),
                anEvent().withId("bau").withNumericId(5).withTitle("Cane AAAAAAH").build()
        );

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Mock
        SearchResults searchResults;

        ItemsAdapter itemsAdapter;

        @Before
        public void setUp() {
            itemsAdapter = new ItemsAdapter(searchResults);
        }

        void givenEmptySearchResults() {
            givenSearchResultsWith(NO_EVENTS, NO_SPEAKERS);
        }

        void givenSearchResultsWith(List<Event> events, List<Speaker> speakers) {
            given(searchResults.events()).willReturn(events);
            given(searchResults.speakers()).willReturn(speakers);
            given(searchResults.isEmpty()).willReturn(events.isEmpty() && speakers.isEmpty());
        }
    }
}
