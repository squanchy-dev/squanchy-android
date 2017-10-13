package net.squanchy.injection;

import android.app.Application;

import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.AnalyticsModule;
import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.remoteconfig.RemoteConfigModule;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.FirebaseModule;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.service.repository.VenueRepository;
import net.squanchy.service.repository.injection.RepositoryModule;
import net.squanchy.support.injection.ChecksumModule;
import net.squanchy.support.injection.CurrentTimeModule;

import dagger.Component;

@ApplicationLifecycle
@Component(modules = {
        ApplicationContextModule.class,
        FirebaseModule.class,
        ChecksumModule.class,
        RepositoryModule.class,
        AnalyticsModule.class,
        RemoteConfigModule.class,
        CurrentTimeModule.class})
public interface ApplicationComponent {

    FirebaseDbService firebaseDbService();

    FirebaseAuthService firebaseAuthService();

    EventRepository eventRepository();

    SpeakerRepository speakerRepository();

    VenueRepository venueRepository();

    Analytics analytics();

    RemoteConfig remoteConfig();

    Application application();

    class Factory {

        private Factory() {
            // non-instantiable
        }

        public static ApplicationComponent create(Application application) {
            return DaggerApplicationComponent.builder()
                    .firebaseModule(new FirebaseModule())
                    .repositoryModule(new RepositoryModule())
                    .checksumModule(new ChecksumModule())
                    .applicationContextModule(new ApplicationContextModule(application))
                    .analyticsModule(new AnalyticsModule(application))
                    .remoteConfigModule(new RemoteConfigModule())
                    .build();
        }
    }
}
