package net.squanchy.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import net.squanchy.service.firebase.FirebaseAuthService;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

class SignInService {

    private final FirebaseAuthService authService;

    SignInService(FirebaseAuthService authService) {
        this.authService = authService;
    }

    Completable signInWithGoogle(GoogleSignInAccount account) {
        return authService.signInWithGoogle(account)
                .subscribeOn(Schedulers.io());
    }
}
