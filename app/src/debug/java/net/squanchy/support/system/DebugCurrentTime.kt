package net.squanchy.support.system

import org.joda.time.DateTime

class DebugCurrentTime : CurrentTime {

    private val mockTime = DateTime.parse("2017-04-06T10:00:00.000+02:00")

    override fun currentDateTime(): DateTime = mockTime
}
