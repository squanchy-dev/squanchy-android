package net.squanchy.schedule.domain.view

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class ScheduleTest {

    @Test
    fun `should be empty when it contains no pages`() {
        val schedule = aSchedule(emptyList())

        val empty = schedule.isEmpty

        assertThat(empty).isTrue
    }

    @Test
    fun `should be empty when it contains only empty pages`() {
        val schedule = aSchedule(listOf(aSchedulePage(events = emptyList()), aSchedulePage(events = emptyList())))

        val empty = schedule.isEmpty

        assertThat(empty).isTrue
    }

    @Test
    fun `should not be empty when its pages contain at least one event`() {
        val schedule = aSchedule(listOf(aSchedulePage(events = listOf(anEvent())), aSchedulePage(events = emptyList())))

        val empty = schedule.isEmpty

        assertThat(empty).isFalse
    }
}
