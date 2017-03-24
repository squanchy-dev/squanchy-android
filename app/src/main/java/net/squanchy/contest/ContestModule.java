package net.squanchy.contest;

import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.firebase.FirebaseDbService;

import dagger.Module;
import dagger.Provides;

@Module
public class ContestModule {

    @Provides
    ContestService contestService(RemoteConfig remoteConfig, FirebaseDbService dbService, FirebaseAuthService authService) {
        return new ContestService(remoteConfig, dbService, authService);
    }
}
