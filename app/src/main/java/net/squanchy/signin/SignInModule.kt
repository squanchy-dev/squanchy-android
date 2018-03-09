package net.squanchy.signin

import net.squanchy.service.firestore.FirebaseAuthService

import dagger.Module
import dagger.Provides

@Module
internal class SignInModule {

    @Provides
    fun service(authService: FirebaseAuthService) = SignInService(authService)
}
