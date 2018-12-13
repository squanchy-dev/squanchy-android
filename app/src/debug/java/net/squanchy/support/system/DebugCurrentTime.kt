package net.squanchy.support.system

import org.threeten.bp.ZonedDateTime

class DebugCurrentTime : CurrentTime {

    private val mockTime = ZonedDateTime.parse("2017-04-06T10:00:00.000+02:00")

    override fun currentDateTime(): ZonedDateTime = mockTime
}
