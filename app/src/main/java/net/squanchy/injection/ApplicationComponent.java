package net.squanchy.injection;

import net.squanchy.SquanchyApplication;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.injection.DbServiceType;
import net.squanchy.service.firebase.injection.FirebaseModule;

import dagger.Component;

@ApplicationLifecycle
@Component(modules = {FirebaseModule.class})
public interface ApplicationComponent {

    @DbServiceType(DbServiceType.Type.AUTHENTICATED)
    FirebaseDbService firebaseDbService();

    class Factory {

        private Factory() {
            // non-instantiable
        }

        public static ApplicationComponent create(SquanchyApplication application) {
            return DaggerApplicationComponent.builder()
                    .firebaseModule(new FirebaseModule())
                    .build();
        }
    }
}
