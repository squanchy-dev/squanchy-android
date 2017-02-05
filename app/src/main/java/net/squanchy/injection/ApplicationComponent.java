package net.squanchy.injection;

import net.squanchy.ConnfaApplication;
import net.squanchy.service.firebase.FirebaseConnfaRepository;
import net.squanchy.service.firebase.injection.FirebaseModule;

import dagger.Component;

@ApplicationLifecycle
@Component(modules = {FirebaseModule.class})
public interface ApplicationComponent {

    FirebaseConnfaRepository firebaseConnfaRepository();

    class Factory {

        private Factory() {
            // non-instantiable
        }

        public static ApplicationComponent create(ConnfaApplication application) {
            return DaggerApplicationComponent.builder()
                    .firebaseModule(new FirebaseModule())
                    .build();
        }
    }
}
