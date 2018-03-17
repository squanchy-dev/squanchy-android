package net.squanchy.search.engines

import com.google.common.truth.Truth.assertThat
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.anEvent
import org.junit.Test

class EventSearchEngineTest {

    private val searchEngine = EventSearchEngine()

    @Test
    fun givenEmptyQuery_whenMatching_thenReturnsFalse() {
        val query = ""

        val matches = searchEngine.matches(ANY_EVENT, query)

        assertThat(matches).isFalse()
    }

    @Test
    fun givenQueryShorterThanTwoCharacters_whenMatching_thenReturnsFalse() {
        val query = "e"

        val matches = searchEngine.matches(MATCHING_EVENT, query)

        assertThat(matches).isFalse()
    }

    @Test
    fun givenOfAtLeastTwoCharacters_whenMatching_thenReturnsTrue() {
        val query = "ne"

        val matches = searchEngine.matches(MATCHING_EVENT, query)

        assertThat(matches).isTrue()
    }

    @Test
    fun givenEventWithCoffeeBreakType_whenMatching_thenReturnsFalse() {
        val event = anEvent(title = "anything", type = Event.Type.COFFEE_BREAK)

        val matches = searchEngine.matches(event, "anything")

        assertThat(matches).isFalse()
    }

    @Test
    fun givenEventWithLunchType_whenMatching_thenReturnsFalse() {
        val event = anEvent(title = "anything", type = Event.Type.LUNCH)

        val matches = searchEngine.matches(event, "anything")

        assertThat(matches).isFalse()
    }

    @Test
    fun givenEventWithRegistrationType_whenMatching_thenReturnsFalse() {
        val event = anEvent(title = "anything", type = Event.Type.REGISTRATION)

        val matches = searchEngine.matches(event, "anything")

        assertThat(matches).isFalse()
    }

    @Test
    fun givenEventWithSocialType_whenMatching_thenReturnsFalse() {
        val event = anEvent(title = "anything", type = Event.Type.SOCIAL)

        val matches = searchEngine.matches(event, "anything")

        assertThat(matches).isFalse()
    }

    @Test
    fun givenEventWithOtherType_whenMatching_thenReturnsFalse() {
        val event = anEvent(title = "anything", type = Event.Type.OTHER)

        val matches = searchEngine.matches(event, "anything")

        assertThat(matches).isFalse()
    }

    @Test
    fun givenEventWithTitleMatchingQueryExactly_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_EVENT, "Banane")

        assertThat(matches).isTrue()
    }

    @Test
    fun givenEventWithTitleMatchingQueryExactly_butWithDifferentCasing_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_EVENT, "banAne")

        assertThat(matches).isTrue()
    }

    @Test
    fun givenEventWithTitleContainingQuery_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_EVENT, "Banane")

        assertThat(matches).isTrue()
    }

    @Test
    fun givenEventWithTitleMatchingQueryExactly_butWithDiacritics_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_EVENT, "BanÀne")

        assertThat(matches).isTrue()
    }

    companion object {

        private val ANY_EVENT = anEvent()

        private val MATCHING_EVENT = anEvent(title = "Banane", type = Event.Type.TALK)
    }
}
