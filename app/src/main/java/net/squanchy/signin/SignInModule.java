package net.squanchy.signin;

import net.squanchy.service.firebase.FirebaseAuthService;

import dagger.Module;
import dagger.Provides;

@Module
public class SignInModule {

    @Provides
    SignInService service(FirebaseAuthService authService) {
        return new SignInService(authService);
    }
}
