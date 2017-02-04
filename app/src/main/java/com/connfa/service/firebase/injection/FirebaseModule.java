package com.connfa.service.firebase.injection;

import com.connfa.injection.ApplicationLifecycle;
import com.connfa.service.firebase.FirebaseConnfaRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    @Provides
    DatabaseReference databaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @ApplicationLifecycle
    @Provides
    FirebaseConnfaRepository firebaseConnfaRepository(DatabaseReference database) {
        return new FirebaseConnfaRepository(database);
    }
}
