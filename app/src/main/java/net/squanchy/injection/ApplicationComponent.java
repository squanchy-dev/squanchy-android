package net.squanchy.injection;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.service.firebase.injection.FirebaseModule;
import net.squanchy.service.view.EventService;
import net.squanchy.support.injection.ChecksumModule;
import net.squanchy.support.lang.Checksum;

import dagger.Component;

@ApplicationLifecycle
@Component(modules = {FirebaseModule.class, ChecksumModule.class})
public interface ApplicationComponent {

    @DbServiceType(DbServiceType.Type.AUTHENTICATED)
    FirebaseDbService firebaseDbService();

    Checksum checksum();

    EventService eventService();

    class Factory {

        private Factory() {
            // non-instantiable
        }

        public static ApplicationComponent create() {
            return DaggerApplicationComponent.builder()
                    .firebaseModule(new FirebaseModule())
                    .checksumModule(new ChecksumModule())
                    .build();
        }
    }
}
