package net.squanchy.support.checksum

import dagger.Module
import dagger.Provides

@Module
class ChecksumModule {

    @Provides
    internal fun checksum() = Checksum()
}
