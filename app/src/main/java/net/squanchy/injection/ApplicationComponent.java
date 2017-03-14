package net.squanchy.injection;

import android.content.Context;

import net.squanchy.proximity.ProximityProvider;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.service.firebase.injection.FirebaseModule;
import net.squanchy.service.proximity.injection.ProximityModule;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.service.repository.injection.RepositoryModule;
import net.squanchy.support.injection.ChecksumModule;
import net.squanchy.support.lang.Checksum;

import dagger.Component;

@ApplicationLifecycle
@Component(modules = {FirebaseModule.class, ChecksumModule.class, RepositoryModule.class, ProximityModule.class})
public interface ApplicationComponent {

    @DbServiceType(DbServiceType.Type.AUTHENTICATED)
    FirebaseDbService firebaseDbService();

    Checksum checksum();

    EventRepository eventRepository();

    SpeakerRepository speakerRepository();

    ProximityProvider proximityProvider();

    class Factory {

        private Factory() {
            // non-instantiable
        }

        public static ApplicationComponent create(Context context) {
            return DaggerApplicationComponent.builder()
                    .firebaseModule(new FirebaseModule())
                    .repositoryModule(new RepositoryModule())
                    .checksumModule(new ChecksumModule())
                    .proximityModule(new ProximityModule(context))
                    .build();
        }
    }
}
