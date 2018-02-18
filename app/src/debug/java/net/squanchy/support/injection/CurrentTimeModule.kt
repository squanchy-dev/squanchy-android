package net.squanchy.support.injection

import dagger.Module
import dagger.Provides
import net.squanchy.support.system.CurrentTime
import net.squanchy.support.system.DebugCurrentTime

@Module
class CurrentTimeModule {

    @Provides
    internal fun provideCurrentTime(): CurrentTime = DebugCurrentTime()
}
