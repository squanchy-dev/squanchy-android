package net.squanchy.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SignInService {

    private final FirebaseAuthService authService;

    SignInService(FirebaseAuthService authService) {
        this.authService = authService;
    }

    public Completable signInAnonymouslyIfNecessary() {
        return authService.currentUser()
                .firstOrError()
                .flatMapCompletable(user -> {
                    if (user.isPresent()) {
                        return Completable.complete();
                    }

                    return authService.signInAnonymously();
                });
    }

    public Observable<Optional<FirebaseUser>> currentUser() {
        return authService.currentUser()
                .subscribeOn(Schedulers.io());
    }

    Completable signInWithGoogle(GoogleSignInAccount account) {
        return authService.signInWithGoogle(account)
                .subscribeOn(Schedulers.io());
    }

    public Completable signOut() {
        return authService.signOut()
                .subscribeOn(Schedulers.io());
    }
}
