package net.squanchy.support.injection

import dagger.Module
import dagger.Provides
import net.squanchy.support.lang.Checksum

@Module
class ChecksumModule {

    @Provides
    internal fun checksum() = Checksum()
}
