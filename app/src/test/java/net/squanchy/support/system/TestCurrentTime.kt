package net.squanchy.support.system

import org.threeten.bp.ZonedDateTime

class TestCurrentTime(private val time: ZonedDateTime) : CurrentTime {

    override fun currentDateTime() = time
}
