package net.squanchy.support.injection

import net.squanchy.support.lang.Checksum

import dagger.Module
import dagger.Provides

@Module
class ChecksumModule {

    @Provides
    internal fun provideChecksum(): Checksum {
        return Checksum()
    }
}
