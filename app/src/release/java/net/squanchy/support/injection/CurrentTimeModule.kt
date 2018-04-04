package net.squanchy.support.injection

import dagger.Module
import dagger.Provides
import net.squanchy.support.system.AndroidCurrentTime
import net.squanchy.support.system.CurrentTime

@Module
class CurrentTimeModule {

    @Provides
    internal fun currentTime(): CurrentTime = AndroidCurrentTime()
}
