package net.squanchy.support.injection;

import net.squanchy.support.lang.Checksum;

import dagger.Module;
import dagger.Provides;

@Module
public class ChecksumModule {

    @Provides
    Checksum provideChecksum() {
        return new Checksum();
    }
}
