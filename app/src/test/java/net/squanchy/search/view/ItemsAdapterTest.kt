package net.squanchy.search.view

import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.anEvent
import net.squanchy.search.SearchResults
import net.squanchy.search.view.SearchAdapter.ViewTypeId
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.speaker.domain.view.SpeakerFixtures.aSpeaker
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit

@RunWith(Enclosed::class)
class ItemsAdapterTest {

    class TotalItemsCount : BaseTest() {

        @Test
        fun givenEmptySearchResults_whenGettingTotalItemsCount_thenReturnsZero() {
            givenEmptySearchResults()

            val totalItemsCount = itemsAdapter.totalItemsCount()

            assertThat(totalItemsCount).isZero
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingTotalItemsCount_thenReturnsTheNumberOfSpeakersPlusOne() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)

            val totalItemsCount = itemsAdapter.totalItemsCount()

            assertThat(totalItemsCount).isEqualTo(4) // 4 = 1 header + 3 events
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingTotalItemsCount_thenReturnsTheNumberOfSpeakersPlusOne() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val totalItemsCount = itemsAdapter.totalItemsCount()

            assertThat(totalItemsCount).isEqualTo(3) // 3 = 1 header + 2 speakers
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingTotalItemsCount_thenReturnsTheNumberOfEventsAndSpeakersPlusTwo() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val totalItemsCount = itemsAdapter.totalItemsCount()

            assertThat(totalItemsCount).isEqualTo(7) // 7 = 1 header + 3 events + 1 header + 2 speakers
        }
    }

    class ViewType : BaseTest() {

        @Test
        fun givenEmptySearchResults_whenGettingViewTypeAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults()
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.viewTypeAtAbsolutePosition(0)
        }

        @Test
        fun givenAnySearchResults_whenGettingViewTypeAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.viewTypeAtAbsolutePosition(-1)
        }

        @Test
        fun givenAnySearchResults_whenGettingViewTypePositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.viewTypeAtAbsolutePosition(7) // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingViewTypeAtEventsHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER)
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingViewTypeAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(1) // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.EVENT)
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingViewTypeAtSpeakerHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER)
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingViewTypeAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(1) // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.SPEAKER)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtEventsHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(1) // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.EVENT)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtSpeakerHeaderPosition_thenReturnsHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(4) // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.HEADER)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingViewTypeAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val viewType = itemsAdapter.viewTypeAtAbsolutePosition(5) // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]

            assertThat(viewType).isEqualTo(ViewTypeId.SPEAKER)
        }
    }

    class ItemId : BaseTest() {

        @Test
        fun givenEmptySearchResults_whenGettingItemIdAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults()
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.itemIdAtAbsolutePosition(0)
        }

        @Test
        fun givenAnySearchResults_whenGettingItemIdAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.itemIdAtAbsolutePosition(-1)
        }

        @Test
        fun givenAnySearchResults_whenGettingItemIdPositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.itemIdAtAbsolutePosition(7) // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingItemIdAtEventsHeaderPosition_thenReturnsEventsHeaderId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-100)
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingItemIdAtEventPosition_thenReturnsEventNumericId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(1) // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS[0].numericId)
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingItemIdAtSpeakerHeaderPosition_thenReturnsSpeakerHeaderId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-101)
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingItemIdAtSpeakerPosition_thenReturnsSpeakerNumericId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(1) // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS[0].numericId)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtEventsHeaderPosition_thenReturnsEventHeaderId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-100)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtEventPosition_thenReturnsEventNumericId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(1) // 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS[0].numericId)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtSpeakerHeaderPosition_thenReturnsSpeakerHeaderId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(4) // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(-101)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingItemIdAtSpeakerPosition_thenReturnsSpeakerNumericId() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val itemId = itemsAdapter.itemIdAtAbsolutePosition(5) // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]

            assertThat(itemId).isEqualTo(ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS[0].numericId)
        }
    }

    class SpeakerAtPosition : BaseTest() {

        @Test
        fun givenEmptySearchResults_whenGettingSpeakerAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults()
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(0)
        }

        @Test
        fun givenAnySearchResults_whenGettingSpeakerAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(-1)
        }

        @Test
        fun givenAnySearchResults_whenGettingSpeakerPositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(7) // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingSpeakerAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(0)
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingSpeakerAtSpeakerHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingSpeakerAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val speaker = itemsAdapter.speakerAtAbsolutePosition(1) // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(speaker).isEqualTo(ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS[0])
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtEventsHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtEventPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(1) // 1 = (1 header + 1 event) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtSpeakerHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.speakerAtAbsolutePosition(4) // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingSpeakerAtSpeakerPosition_thenReturnsSpeaker() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val speaker = itemsAdapter.speakerAtAbsolutePosition(5) // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]

            assertThat(speaker).isEqualTo(ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS[0])
        }
    }

    class EventAtPosition : BaseTest() {

        @Test
        fun givenEmptySearchResults_whenGettingEventAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults()
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(0)
        }

        @Test
        fun givenAnySearchResults_whenGettingEventAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(-1)
        }

        @Test
        fun givenAnySearchResults_whenGettingEventPositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(7) // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingEventAtEventsHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingEventAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)

            val event = itemsAdapter.eventAtAbsolutePosition(1) // 1 = (1 header + 1 speaker) - 1 [because zero-based]

            assertThat(event).isEqualTo(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS[0])
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingEventAtSpeakersHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(4) // 4 = (1 header + 3 speaker + 1 header) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingEventAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(0)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtEventsHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtEventPosition_thenReturnsEvent() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val event = itemsAdapter.eventAtAbsolutePosition(1)// 1 = (1 header + 1 event) - 1 [because zero-based]

            assertThat(event).isEqualTo(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS[0])
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtSpeakerHeaderPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(4) // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingEventAtSpeakerPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.eventAtAbsolutePosition(5) // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]
        }
    }

    class HeaderTypeAtPosition : BaseTest() {

        @Test
        fun givenEmptySearchResults_whenGettingHeaderTypeAtAnyPosition_thenThrowsIndexOutOfBoundsException() {
            givenEmptySearchResults()
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.headerTypeAtAbsolutePosition(0)
        }

        @Test
        fun givenAnySearchResults_whenGettingHeaderTypeAtNegativePosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.headerTypeAtAbsolutePosition(-1)
        }

        @Test
        fun givenAnySearchResults_whenGettingHeaderTypePositionEqualOrGreaterThanTotalItemsCount_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.headerTypeAtAbsolutePosition(7) // 7 = (1 header + 3 events + 1 header + 2 speakers + 1 off-by-one) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingHeaderTypeAtEventsHeaderPosition_thenReturnsEventsHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)

            val headerType = itemsAdapter.headerTypeAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.EVENTS)
        }

        @Test
        fun givenSearchResultsWithOnlyEvents_whenGettingHeaderTypeAtEventPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.NO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.headerTypeAtAbsolutePosition(1) // 1 = (1 header + 1 speaker) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingHeaderTypeAtSpeakerHeaderPosition_thenReturnsSpeakersHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val headerType = itemsAdapter.headerTypeAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.SPEAKERS)
        }

        @Test
        fun givenSearchResultsWithOnlySpeakers_whenGettingHeaderTypeAtSpeakerPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.NO_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.headerTypeAtAbsolutePosition(1) // 1 = (1 header + 1 speaker) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtEventsHeaderPosition_thenReturnsEventsHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val headerType = itemsAdapter.headerTypeAtAbsolutePosition(0) // 0 = (1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.EVENTS)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtEventPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.headerTypeAtAbsolutePosition(1) // 1 = (1 header + 1 event) - 1 [because zero-based]
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtSpeakerHeaderPosition_thenReturnsSpeakersHeader() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)

            val headerType = itemsAdapter.headerTypeAtAbsolutePosition(4) // 4 = (1 header + 3 events + 1 header) - 1 [because zero-based]

            assertThat(headerType).isEqualTo(HeaderType.SPEAKERS)
        }

        @Test
        fun givenSearchResultsWithEventsAndSpeakers_whenGettingHeaderTypeAtSpeakerPosition_thenThrowsIndexOutOfBoundsException() {
            givenSearchResultsWith(ItemsAdapterTest.BaseTest.ANY_THREE_EVENTS, ItemsAdapterTest.BaseTest.ANY_TWO_SPEAKERS)
            thrown.expect(IndexOutOfBoundsException::class.java)

            itemsAdapter.headerTypeAtAbsolutePosition(5) // 5 = (1 header + 3 events + 1 header + 1 speaker) - 1 [because zero-based]
        }
    }

    abstract class BaseTest {

        @Rule
        @JvmField
        var mockitoRule = MockitoJUnit.rule()

        @Rule
        @JvmField
        var thrown = ExpectedException.none()

        internal lateinit var itemsAdapter: ItemsAdapter

        internal fun givenEmptySearchResults() {
            givenSearchResultsWith(NO_EVENTS, NO_SPEAKERS)
        }

        internal fun givenSearchResultsWith(events: List<Event>, speakers: List<Speaker>) {
            val searchResults = SearchResults.create(events, speakers)
            itemsAdapter = ItemsAdapter(searchResults)
        }

        companion object {

            internal val NO_EVENTS = emptyList<Event>()
            internal val NO_SPEAKERS = emptyList<Speaker>()

            internal val ANY_TWO_SPEAKERS = listOf(
                aSpeaker().withId("banana").withNumericId(1).withName("Banana Joe").build(),
                aSpeaker().withId("potato").withNumericId(2).withName("Detective Patatorfio").build()
            )
            internal val ANY_THREE_EVENTS: List<Event> = listOf(
                anEvent(id = "carrot", numericId = 3, title = "Karotoff"),
                anEvent(id = "johnnyfer", numericId = 4, title = "Johnnyfer Jaypegg"),
                anEvent(id = "bau", numericId = 5, title = "Cane AAAAAAH")
            )
        }
    }
}
