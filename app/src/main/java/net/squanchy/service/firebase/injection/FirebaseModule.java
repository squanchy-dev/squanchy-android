package net.squanchy.service.firebase.injection;

import net.squanchy.injection.ApplicationLifecycle;
import net.squanchy.service.firebase.FirebaseConnfaRepository;
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
