package net.squanchy.service.firebase.injection;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.squanchy.injection.ApplicationLifecycle;
import net.squanchy.service.firebase.FirebaseSquanchyRepository;

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
    FirebaseSquanchyRepository firebaseSquanchyRepository(DatabaseReference database) {
        return new FirebaseSquanchyRepository(database);
    }
}
