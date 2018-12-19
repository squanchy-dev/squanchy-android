package net.squanchy.support.injection

import android.app.Application
import dagger.Module
import dagger.Provides
import net.squanchy.support.system.CurrentTime
import net.squanchy.support.system.DebugCurrentTime

@Module
class CurrentTimeModule {

    @Provides
    internal fun currentTime(application: Application): CurrentTime = DebugCurrentTime(application)
}
