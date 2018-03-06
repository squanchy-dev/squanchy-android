package net.squanchy.search.view;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.search.SearchResults;
import net.squanchy.search.view.SearchAdapter.ViewTypeId;
import net.squanchy.speaker.domain.view.Speaker;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static net.squanchy.schedule.domain.view.EventFixtures.anEvent;
import static net.squanchy.speaker.domain.view.SpeakerFixtures.aSpeaker;

@RunWith(Enclosed.class)
public class ItemsAdapterTest {

    public static class TotalItemsCount extends BaseTest {

        @Test
        public void givenEmptySearchResults_whenGettingTotalItemsCount_thenReturnsZero() {
            givenEmptySearchResults();

            int totalItemsCount = itemsAdapter.totalItemsCount();

            assertThat(totalItemsCount).isEqualTo(0);
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingTotalItemsCount_thenReturnsTheNumberOfSpeakersPlusOne() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

            int totalItemsCount = itemsAdapter.totalItemsCount();

            assertThat(totalItemsCount).isEqualTo(4);      // 4 = 1 header + 3 events
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingTotalItemsCount_thenReturnsTheNumberOfSpeakersPlusOne() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            int totalItemsCount = itemsAdapter.totalItemsCount();

            assertThat(totalItemsCount).isEqualTo(3);      // 3 = 1 header + 2 speakers
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingTotalItemsCount_thenReturnsTheNumberOfEventsAndSpeakersPlusTwo() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            int totalItemsCount = itemsAdapter.totalItemsCount();

            assertThat(totalItemsCount).isEqualTo(7);      // 7 = 1 header + 3 events + 1 header + 2 speakers
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

    public static class ItemId extends BaseTest {

        @Test
        public void givenEmptySearchResults_whenGettingItemIdAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults();
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.itemIdAtAbsolutePosition(0);
        }

        @Test
        public void givenAnySearchResults_whenGettingItemIdAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.itemIdAtAbsolutePosition(-1);
        }

        @Test
        public void givenAnySearchResults_whenGettingItemIdPositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.itemIdAtAbsolutePosition(7);      // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingItemIdAtEventsHeaderPosition_thenReturnsEventsHeaderId() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-100);
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingItemIdAtEventPosition_thenReturnsEventNumericId() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(1);      // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ANY_THREE_EVENTS.get(0).getNumericId());
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingItemIdAtSpeakerHeaderPosition_thenReturnsSpeakerHeaderId() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-101);
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingItemIdAtSpeakerPosition_thenReturnsSpeakerNumericId() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(1);      // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ANY_TWO_SPEAKERS.get(0).getNumericId());
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtEventsHeaderPosition_thenReturnsEventHeaderId() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-100);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtEventPosition_thenReturnsEventNumericId() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(1);      // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ANY_THREE_EVENTS.get(0).getNumericId());
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtSpeakerHeaderPosition_thenReturnsSpeakerHeaderId() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(4);      // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-101);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtSpeakerPosition_thenReturnsSpeakerNumericId() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            long itemId = itemsAdapter.itemIdAtAbsolutePosition(5);     // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ANY_TWO_SPEAKERS.get(0).getNumericId());
        }
    }

    public static class SpeakerAtPosition extends BaseTest {

        @Test
        public void givenEmptySearchResults_whenGettingSpeakerAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults();
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(0);
        }

        @Test
        public void givenAnySearchResults_whenGettingSpeakerAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(-1);
        }

        @Test
        public void givenAnySearchResults_whenGettingSpeakerPositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(7);      // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingSpeakerAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(0);
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingSpeakerAtSpeakerHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingSpeakerAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            Speaker speaker = itemsAdapter.speakerAtAbsolutePosition(1);      // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(speaker).isEqualTo(ANY_TWO_SPEAKERS.get(0));
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtEventsHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtEventPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(1);      // 1 = (1 header + 1 event) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtSpeakerHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.speakerAtAbsolutePosition(4);      // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            Speaker speaker = itemsAdapter.speakerAtAbsolutePosition(5);     // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]

            assertThat(speaker).isEqualTo(ANY_TWO_SPEAKERS.get(0));
        }
    }

    public static class EventAtPosition extends BaseTest {

        @Test
        public void givenEmptySearchResults_whenGettingEventAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults();
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(0);
        }

        @Test
        public void givenAnySearchResults_whenGettingEventAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(-1);
        }

        @Test
        public void givenAnySearchResults_whenGettingEventPositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(7);      // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingEventAtEventsHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingEventAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

            Event event = itemsAdapter.eventAtAbsolutePosition(1);      // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(event).isEqualTo(ANY_THREE_EVENTS.get(0));
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingEventAtSpeakersHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(4);      // 4 = (1 header + 3 speaker + 1 header) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingEventAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(0);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtEventsHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            Event event = itemsAdapter.eventAtAbsolutePosition(1);// 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(event).isEqualTo(ANY_THREE_EVENTS.get(0));
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtSpeakerHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(4);      // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtSpeakerPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.eventAtAbsolutePosition(5);     // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]
        }
    }

    public static class HeaderTypeAtPosition extends BaseTest {

        @Test
        public void givenEmptySearchResults_whenGettingHeaderTypeAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults();
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.headerTypeAtAbsolutePosition(0);
        }

        @Test
        public void givenAnySearchResults_whenGettingHeaderTypeAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.headerTypeAtAbsolutePosition(-1);
        }

        @Test
        public void givenAnySearchResults_whenGettingHeaderTypePositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.headerTypeAtAbsolutePosition(7);      // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingHeaderTypeAtEventsHeaderPosition_thenReturnsEventsHeader() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);

            HeaderType headerType = itemsAdapter.headerTypeAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.EVENTS);
        }

        @Test
        public void givenSearchResultsWithOnlyEvents_whenGettingHeaderTypeAtEventPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, NO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.headerTypeAtAbsolutePosition(1);      // 1 = (1 header + 1 speaker) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingHeaderTypeAtSpeakerHeaderPosition_thenReturnsSpeakersHeader() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);

            HeaderType headerType = itemsAdapter.headerTypeAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.SPEAKERS);
        }

        @Test
        public void givenSearchResultsWithOnlySpeakers_whenGettingHeaderTypeAtSpeakerPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(NO_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.headerTypeAtAbsolutePosition(1);      // 1 = (1 header + 1 speaker) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtEventsHeaderPosition_thenReturnsEventsHeader() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            HeaderType headerType = itemsAdapter.headerTypeAtAbsolutePosition(0);      // 0 = (1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.EVENTS);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtEventPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.headerTypeAtAbsolutePosition(1);      // 1 = (1 header + 1 event) - 1 [because zero-based]
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtSpeakerHeaderPosition_thenReturnsSpeakersHeader() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);

            HeaderType headerType = itemsAdapter.headerTypeAtAbsolutePosition(4);      // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.SPEAKERS);
        }

        @Test
        public void givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtSpeakerPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ANY_THREE_EVENTS, ANY_TWO_SPEAKERS);
            thrown.expect(IndexOutOfBoundsException.class);

            itemsAdapter.headerTypeAtAbsolutePosition(5);      // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]
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

        ItemsAdapter itemsAdapter;

        void givenEmptySearchResults() {
            givenSearchResultsWith(NO_EVENTS, NO_SPEAKERS);
        }

        void givenSearchResultsWith(List<Event> events, List<Speaker> speakers) {
            SearchResults searchResults = SearchResults.Companion.create(events, speakers);
            itemsAdapter = new ItemsAdapter(searchResults);
        }
    }
}
