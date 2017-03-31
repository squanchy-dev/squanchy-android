package net.squanchy.home;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.support.system.CurrentTime;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrentEventModule {

    @Provides
    CurrentEventSnackbarService currentEventService(EventRepository eventRepository,
                                                    FirebaseAuthService firebaseAuthService,
                                                    CurrentTime currentTime) {
        return new CurrentEventSnackbarService(eventRepository, firebaseAuthService, currentTime);
    }
}
