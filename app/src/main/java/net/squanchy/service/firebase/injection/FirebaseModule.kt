package net.squanchy.service.firebase.injection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import net.squanchy.injection.ApplicationLifecycle
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirebaseDbService

@Module
class FirebaseModule {

    @Provides
    internal fun databaseReference(): DatabaseReference=  FirebaseDatabase.getInstance().reference

    @Provides
    internal fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @ApplicationLifecycle
    @Provides
    internal fun firebaseAuthService(firebaseAuth: FirebaseAuth): FirebaseAuthService = FirebaseAuthService(firebaseAuth)

    @ApplicationLifecycle
    @Provides
    internal fun firebaseDbService(database: DatabaseReference): FirebaseDbService = FirebaseDbService(database)
}
