package net.squanchy.support.injection

import net.squanchy.support.system.CurrentTime

import dagger.Module
import dagger.Provides

@Module
class CurrentTimeModule {

    @Provides
    internal fun provideCurrentTime() = CurrentTime()
}
