package net.squanchy.search.view

import java.util.Locale

import net.squanchy.schedule.domain.view.Event
import net.squanchy.search.SearchResult
import net.squanchy.search.view.SearchAdapter.Companion.ViewTypeId
import net.squanchy.speaker.domain.view.Speaker

internal class ItemsAdapter(private val searchResult: SearchResult.Success) {

    val isEmpty: Boolean
        get() = searchResult.isEmpty

    fun totalItemsCount(): Int {
        if (searchResult.isEmpty) {
            return 0
        }

        val totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.events)
        val totalSpeakerItemsCount = totalCountForSectionIncludingHeaders(searchResult.speakers)

        return totalEventItemsCount + totalSpeakerItemsCount
    }

    @ViewTypeId
    fun viewTypeAtAbsolutePosition(position: Int): Int {
        ensurePositionExists(position)

        val totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.events)

        val adjustedPosition: Int
        if (position < totalEventItemsCount) {
            adjustedPosition = position
        } else {
            adjustedPosition = position - totalEventItemsCount
        }

        return when {
            adjustedPosition == 0 -> SearchAdapter.HEADER
            position < totalEventItemsCount -> SearchAdapter.EVENT
            else -> SearchAdapter.SPEAKER
        }
    }

    fun itemIdAtAbsolutePosition(position: Int): Long {
        ensurePositionExists(position)

        val totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.events)
        if (totalEventItemsCount > 0 && position < totalEventItemsCount) {
            return if (position == 0) ITEM_ID_EVENTS_HEADER else searchResult.events[position - 1].numericId
        } else {
            val adjustedPosition = position - totalEventItemsCount
            // We checked position is in range so it MUST be a speaker
            return if (adjustedPosition == 0) ITEM_ID_SPEAKERS_HEADER else searchResult.speakers[adjustedPosition - 1].numericId
        }
    }

    fun speakerAtAbsolutePosition(position: Int): Speaker {
        ensurePositionExists(position)

        val totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.events)
        if (totalEventItemsCount > 0 && position < totalEventItemsCount) {
            throw IndexOutOfBoundsException("No speaker at position $position, that is supposed to be in the events sublist")
        }

        val adjustedPosition = position - totalEventItemsCount
        // We checked position is in range so it MUST be a speaker
        return if (adjustedPosition > 0) {
            searchResult.speakers[adjustedPosition - 1]
        } else {
            throw IndexOutOfBoundsException("No speaker at position $position, that is supposed to be the speakers header")
        }
    }

    fun eventAtAbsolutePosition(position: Int): Event {
        ensurePositionExists(position)

        val totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.events)
        if (position == 0) {
            throw IndexOutOfBoundsException("No event at position $position, that is supposed to be the events header")
        } else if (position - 1 >= totalEventItemsCount) {
            throw IndexOutOfBoundsException("No event at position $position, that is supposed to be in the speakers sublist")
        }

        return searchResult.events[position - 1]
    }

    fun headerTypeAtAbsolutePosition(position: Int): HeaderType {
        ensurePositionExists(position)

        val totalEventItemsCount = totalCountForSectionIncludingHeaders(searchResult.events)
        if (totalEventItemsCount > 0) {
            if (position == 0) {
                return HeaderType.EVENTS
            } else if (position < totalEventItemsCount) {
                throw IndexOutOfBoundsException("No header at position $position, that is supposed to be an event")
            }
        }

        val adjustedPosition = position - totalEventItemsCount
        // We checked position is in range so it MUST be a speaker
        return if (adjustedPosition == 0) {
            HeaderType.SPEAKERS
        } else {
            throw IndexOutOfBoundsException("No header at position $position, that is supposed to be a speaker")
        }
    }

    private fun ensurePositionExists(position: Int) {
        val totalItemsCount = totalItemsCount()
        if (position < 0 || position >= totalItemsCount) {
            val message = String.format(Locale.ROOT, "Position %1\$d is not valid, must be [0, %2\$d)", position, totalItemsCount)
            throw IndexOutOfBoundsException(message)
        }
    }

    private fun totalCountForSectionIncludingHeaders(sectionItems: List<*>): Int {
        val eventsCount = sectionItems.size
        return eventsCount + headersCountForSectionItemsCount(eventsCount)
    }

    companion object {

        // These values are "random", we count on them not clashing with the other
        // random values that are used for non-hardcoded numeric IDs (for events
        // and speakers). This is a reasonable assumption in the Long range.
        // In addition, the CRC32 values we use as numeric IDs are always positive.
        private const val ITEM_ID_EVENTS_HEADER: Long = -100
        private const val ITEM_ID_SPEAKERS_HEADER: Long = -101

        private fun headersCountForSectionItemsCount(itemsCount: Int): Int {
            return if (itemsCount > 0) 1 else 0
        }
    }
}
