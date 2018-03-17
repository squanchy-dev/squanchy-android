package net.squanchy.search.engines

import com.google.common.truth.Truth.assertThat
import net.squanchy.speaker.domain.view.aSpeaker
import org.junit.Test

class SpeakerSearchEngineTest {

    private val searchEngine = SpeakerSearchEngine()

    @Test
    fun givenEmptyQuery_whenMatching_thenReturnsFalse() {
        val query = ""

        val matches = searchEngine.matches(ANY_SPEAKER, query)

        assertThat(matches).isFalse()
    }

    @Test
    fun givenQueryShorterThanTwoCharacters_whenMatching_thenReturnsFalse() {
        val query = "a"

        val matches = searchEngine.matches(MATCHING_SPEAKER, query)

        assertThat(matches).isFalse()
    }

    @Test
    fun givenOfAtLeastTwoCharacters_whenMatching_thenReturnsTrue() {
        val query = "na"

        val matches = searchEngine.matches(MATCHING_SPEAKER, query)

        assertThat(matches).isTrue()
    }

    @Test
    fun givenSpeakerWithNameMatchingQueryExactly_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_SPEAKER, "Banana Joe")

        assertThat(matches).isTrue()
    }

    @Test
    fun givenSpeakerWithNameContainingQuery_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_SPEAKER, "anana J")

        assertThat(matches).isTrue()
    }

    @Test
    fun givenSpeakerWithNameMatching_butWithDifferentCasing_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_SPEAKER, "anana J")

        assertThat(matches).isTrue()
    }

    @Test
    fun givenSpeakerWithNameMatchingQueryExactly_butWithDiacritics_whenMatching_thenReturnsTrue() {

        val matches = searchEngine.matches(MATCHING_SPEAKER, "anÀna J")

        assertThat(matches).isTrue()
    }

    companion object {

        private val ANY_SPEAKER = aSpeaker()

        private val MATCHING_SPEAKER = aSpeaker(name = "Banana Joe")
    }
}
