package net.squanchy.service.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;

public class FirebaseAuthService {

    private final FirebaseAuth auth;

    public FirebaseAuthService(FirebaseAuth auth) {
        this.auth = auth;
    }

    public Completable signInWithGoogle(GoogleSignInAccount account) {
        return currentUser()
                .firstOrError()
                .flatMapCompletable(user -> {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    if (user.isPresent()) {
                        return linkAccountWithGoogleCredential(user.get(), credential);
                    } else {
                        return createAccountWithGoogleCredential(credential);
                    }
                });
    }

    private CompletableSource linkAccountWithGoogleCredential(FirebaseUser firebaseUser, AuthCredential credential) {
        return completableObserver -> {
            // TODO user might be already linked, do migration in case
            firebaseUser.linkWithCredential(credential).addOnCompleteListener(result -> {
                if (result.isSuccessful()) {
                    completableObserver.onComplete();
                } else {
                    completableObserver.onError(result.getException());
                }
            });
        };
    }

    private CompletableSource createAccountWithGoogleCredential(AuthCredential credential) {
        return completableObserver -> {
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(result -> {
                        if (result.isSuccessful()) {
                            completableObserver.onComplete();
                        } else {
                            completableObserver.onError(result.getException());
                        }
                    });
        };
    }

    public <T> Observable<T> ifUserSignedInThenObservableFrom(Func1<String, Observable<T>> observableProvider) {
        return ifUserSignedIn()
                .flatMap(user -> observableProvider.call(user.getUid()));
    }

    public Completable ifUserSignedInThenCompletableFrom(Func1<String, Completable> completableProvider) {
        return ifUserSignedIn()
                .flatMapCompletable(user -> completableProvider.call(user.getUid()));
    }

    private Observable<FirebaseUser> ifUserSignedIn() {
        return currentUser()
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Observable<Optional<FirebaseUser>> currentUser() {
        return Observable.create(e -> {
            FirebaseAuth.AuthStateListener listener = firebaseAuth -> e.onNext(Optional.fromNullable(firebaseAuth.getCurrentUser()));

            auth.addAuthStateListener(listener);

            e.setCancellable(() -> auth.removeAuthStateListener(listener));
        });
    }

    public Completable signInAnonymously() {
        return Completable.create(e -> auth.signInAnonymously()
                .addOnSuccessListener(result -> e.onComplete())
                .addOnFailureListener(e::onError));
    }
}
