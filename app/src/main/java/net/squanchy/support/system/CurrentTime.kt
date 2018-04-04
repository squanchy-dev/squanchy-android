package net.squanchy.support.system

import org.joda.time.DateTime

interface CurrentTime {

    fun currentDateTime(): DateTime
}

@Suppress("unused") // It's actually used in the release CurrentTimeModule
class AndroidCurrentTime : CurrentTime {

    override fun currentDateTime(): DateTime = DateTime.now()
}
