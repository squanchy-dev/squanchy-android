package net.squanchy.contest;

import net.squanchy.remoteconfig.RemoteConfig;

import dagger.Module;
import dagger.Provides;

@Module
public class ContestModule {

    @Provides
    ContestService contestService(RemoteConfig remoteConfig) {
        return new ContestService(remoteConfig);
    }
}
