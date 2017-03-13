package net.squanchy.injection;

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
@Component(modules = {FirebaseModule.class, ChecksumModule.class, RepositoryModule.class})
public interface ApplicationComponent {

    @DbServiceType(DbServiceType.Type.AUTHENTICATED)
    FirebaseDbService firebaseDbService();

    Checksum checksum();

    EventRepository eventRepository();

    SpeakerRepository speakerRepository();

    class Factory {

        private Factory() {
            // non-instantiable
        }

        public static ApplicationComponent create() {
            return DaggerApplicationComponent.builder()
                    .firebaseModule(new FirebaseModule())
                    .repositoryModule(new RepositoryModule())
                    .checksumModule(new ChecksumModule())
                    .build();
        }
    }
}
