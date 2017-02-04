package com.connfa.injection;

import com.connfa.ConnfaApplication;
import com.connfa.service.firebase.FirebaseConnfaRepository;
import com.connfa.service.firebase.injection.FirebaseModule;

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
