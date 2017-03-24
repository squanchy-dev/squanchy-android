package net.squanchy.home;

import net.squanchy.service.firebase.FirebaseAuthService;

import dagger.Module;
import dagger.Provides;

@Module
class HomeModule {

    @Provides
    HomeService homeService(FirebaseAuthService authService) {
        return new HomeService(authService);
    }
}
