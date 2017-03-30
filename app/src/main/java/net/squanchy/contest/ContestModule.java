package net.squanchy.contest;

import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.support.system.CurrentTime;

import dagger.Module;
import dagger.Provides;

@Module
public class ContestModule {

    @Provides
    ContestService contestService(RemoteConfig remoteConfig, FirebaseDbService dbService, FirebaseAuthService authService, CurrentTime currentTime) {
        return new ContestService(remoteConfig, dbService, authService, currentTime);
    }
}
