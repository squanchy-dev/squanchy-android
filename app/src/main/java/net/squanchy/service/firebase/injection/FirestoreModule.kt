package net.squanchy.service.firebase.injection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ApplicationLifecycle
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.repository.AuthService

@Module
class FirestoreModule {

    @Provides
    internal fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    internal fun firebaseFirestoreSettings(remoteConfig: RemoteConfig): FirebaseFirestoreSettings {
        val persistenceEnabled = !remoteConfig.getBoolean(KEY_PERSISTENCE_DISABLED)

        return FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(persistenceEnabled)
            .setTimestampsInSnapshotsEnabled(true)
            .build()
    }

    @Provides
    internal fun firebaseFirestore(settings: FirebaseFirestoreSettings): FirebaseFirestore {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.firestoreSettings = settings
        return firebaseFirestore
    }

    @Provides
    @ApplicationLifecycle
    internal fun firestoreDbService(database: FirebaseFirestore): FirestoreDbService = FirestoreDbService(database)

    @ApplicationLifecycle
    @Provides
    internal fun firebaseAuthService(firebaseAuth: FirebaseAuth): AuthService = FirebaseAuthService(firebaseAuth)
}

private const val KEY_PERSISTENCE_DISABLED = "firestore_persistence_disabled"
