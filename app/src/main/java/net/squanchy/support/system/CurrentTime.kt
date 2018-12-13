package net.squanchy.support.system

import org.threeten.bp.ZonedDateTime

interface CurrentTime {

    fun currentDateTime(): ZonedDateTime
}

@Suppress("unused") // It's actually used in the release CurrentTimeModule
class AndroidCurrentTime : CurrentTime {

    override fun currentDateTime(): ZonedDateTime = ZonedDateTime.now()
}
