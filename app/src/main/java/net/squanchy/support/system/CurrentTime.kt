package net.squanchy.support.system

import org.joda.time.LocalDateTime

class CurrentTime {

    fun currentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    fun currentLocalDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}
