package net.squanchy.support.system

import net.squanchy.BuildConfig
import org.joda.time.LocalDateTime

class CurrentTime {

    private val mockTime = LocalDateTime.parse("2017-10-27T12:00:00.000")

    fun currentLocalDateTime(): LocalDateTime = if (BuildConfig.DEBUG) mockTime else LocalDateTime.now()
}
