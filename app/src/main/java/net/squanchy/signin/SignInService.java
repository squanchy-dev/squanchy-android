package net.squanchy.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import net.squanchy.service.firebase.FirebaseAuthService;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class SignInService {

    private final FirebaseAuthService authService;

    SignInService(FirebaseAuthService authService) {
        this.authService = authService;
    }

    public Completable signInAnonymouslyIfNecessary() {
        return authService.currentUser()
                .flatMapCompletable(user -> {
                    if (user.isPresent()) {
                        return Completable.complete();
                    }

                    return authService.signInAnonymously();
                });
    }

    Completable signInWithGoogle(GoogleSignInAccount account) {
        return authService.signInWithGoogle(account)
                .subscribeOn(Schedulers.io());
    }
}
