package net.squanchy.service.firestore.injection

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ApplicationLifecycle
import net.squanchy.service.firestore.FirestoreDbService

@Module
class FirestoreModule {

    @Provides
    internal fun firebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance().apply {
            val settings = FirebaseFirestoreSettings.Builder(firestoreSettings)
                .setPersistenceEnabled(false)
                .build()
            firestoreSettings = settings
        }
    }

    @ApplicationLifecycle
    @Provides
    internal fun firebaseDbService(database: FirebaseFirestore): FirestoreDbService = FirestoreDbService(database)
}
