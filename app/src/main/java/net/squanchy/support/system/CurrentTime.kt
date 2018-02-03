package net.squanchy.support.system

import org.joda.time.DateTime

interface CurrentTime {
    fun currentDateTime(): DateTime
}

class AndroidCurrentTime : CurrentTime {

    override fun currentDateTime(): DateTime = DateTime.now()
}
