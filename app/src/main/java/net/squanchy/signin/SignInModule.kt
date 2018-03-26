package net.squanchy.signin

import dagger.Module
import dagger.Provides
import net.squanchy.service.repository.AuthService

@Module
internal class SignInModule {

    @Provides
    fun service(authService: AuthService) = SignInService(authService)
}
