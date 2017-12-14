package net.squanchy.support.system

import org.joda.time.LocalDateTime

class CurrentTime {

    fun currentTimestamp(): Long = System.currentTimeMillis()

    fun currentLocalDateTime(): LocalDateTime = LocalDateTime.now()
}
