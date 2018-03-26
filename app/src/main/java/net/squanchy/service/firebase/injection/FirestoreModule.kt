package net.squanchy.service.firebase.injection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ApplicationLifecycle
import net.squanchy.service.firebase.FirebaseAuthProvider
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.repository.AuthProvider

@Module
class FirestoreModule {

    @Provides
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun firebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @ApplicationLifecycle
    fun firestoreDbService(database: FirebaseFirestore): FirestoreDbService = FirestoreDbService(database)

    @Provides
    fun authProvider(firebaseAuth: FirebaseAuth): AuthProvider = FirebaseAuthProvider(firebaseAuth)

    @Provides
    fun firebaseAuthService(authProvider: AuthProvider): FirebaseAuthService = FirebaseAuthService(authProvider)
}
