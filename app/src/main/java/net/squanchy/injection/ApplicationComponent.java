package net.squanchy.injection;

import android.app.Application;

import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.AnalyticsModule;
import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.remoteconfig.RemoteConfigModule;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.service.firebase.injection.FirebaseModule;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.service.repository.injection.RepositoryModule;
import net.squanchy.support.injection.ChecksumModule;
import net.squanchy.support.lang.Checksum;

import dagger.Component;

@ApplicationLifecycle
@Component(modules = {FirebaseModule.class, ChecksumModule.class, RepositoryModule.class, AnalyticsModule.class, RemoteConfigModule.class})
public interface ApplicationComponent {

    @DbServiceType(DbServiceType.Type.AUTHENTICATED)
    FirebaseDbService firebaseDbService();

    Checksum checksum();

    EventRepository eventRepository();

    SpeakerRepository speakerRepository();

    Analytics analytics();

    RemoteConfig remoteConfig();

    class Factory {

        private Factory() {
            // non-instantiable
        }

        public static ApplicationComponent create(Application application) {
            return DaggerApplicationComponent.builder()
                    .firebaseModule(new FirebaseModule())
                    .repositoryModule(new RepositoryModule())
                    .checksumModule(new ChecksumModule())
                    .analyticsModule(new AnalyticsModule(application))
                    .remoteConfigModule(new RemoteConfigModule())
                    .build();
        }
    }
}
