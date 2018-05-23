package net.squanchy.eventdetails.domain.view

import com.google.common.truth.Truth.assertThat
import net.squanchy.schedule.domain.view.ExperienceLevel
import net.squanchy.support.lang.getOrThrow
import org.junit.Test

class ExperienceLevelTest {

    @Test
    fun `should return an empty option when tryParsing null string`() {
        val parsed = ExperienceLevel.tryParsingFrom(null)

        assertThat(parsed.isEmpty()).isTrue()
    }

    @Test
    fun `should return an empty option when tryParsing an empty string`() {
        val parsed = ExperienceLevel.tryParsingFrom("")

        assertThat(parsed.isEmpty()).isTrue()
    }

    @Test
    fun `should return an empty option when tryParsing an invalid level string`() {
        val parsed = ExperienceLevel.tryParsingFrom("banana")

        assertThat(parsed.isEmpty()).isTrue()
    }

    @Test
    fun `should return an option of BEGINNER when tryParsing a valid beginner level string`() {
        val parsed = ExperienceLevel.tryParsingFrom("beginner")

        assertThat(parsed.getOrThrow()).isEqualTo(ExperienceLevel.BEGINNER)
    }

    @Test
    fun `should return an option of INTERMEDIATE when tryParsing a valid intermediate level string`() {
        val parsed = ExperienceLevel.tryParsingFrom("intermediate")

        assertThat(parsed.getOrThrow()).isEqualTo(ExperienceLevel.INTERMEDIATE)
    }

    @Test
    fun `should return an option of ADVANCED when tryParsing a valid advanced level string`() {
        val parsed = ExperienceLevel.tryParsingFrom("advanced")

        assertThat(parsed.getOrThrow()).isEqualTo(ExperienceLevel.ADVANCED)
    }
}
