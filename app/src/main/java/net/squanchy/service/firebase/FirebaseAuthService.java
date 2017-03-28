package net.squanchy.service.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

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
                        return signInWithGoogleCredential(credential);
                    }
                });
    }

    private Completable linkAccountWithGoogleCredential(FirebaseUser user, AuthCredential credential) {
        return fromTask(() -> user.linkWithCredential(credential))
                .onErrorResumeNext(deleteUserAndSignInWithCredentialIfLinkingFailed(user, credential));
    }

    private Function<Throwable, CompletableSource> deleteUserAndSignInWithCredentialIfLinkingFailed(FirebaseUser user, AuthCredential credential) {
        return error -> {
            if (linkingFailed(error)) {
                if (!user.isAnonymous()) {
                    return Completable.error(new IllegalStateException("Trying to link user with Google with non anonymous user", error));
                }

                return deleteUser(user)
                        .andThen(signInWithGoogleCredential(credential));
            }

            return Completable.error(error);
        };
    }

    private boolean linkingFailed(Throwable error) {
        return error instanceof FirebaseAuthUserCollisionException;
    }

    private Completable signInWithGoogleCredential(AuthCredential credential) {
        return fromTask(() -> auth.signInWithCredential(credential));
    }

    private Completable deleteUser(FirebaseUser user) {
        return fromTask(user::delete);
    }

    private <T> Completable fromTask(TaskProvider<T> taskProvider) {
        return Completable.create(completableObserver -> taskProvider.task().addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                completableObserver.onComplete();
            } else {
                completableObserver.onError(result.getException());
            }
        }));
    }

    public <T> Observable<T> ifUserSignedInThenObservableFrom(Func1<String, Observable<T>> observableProvider) {
        return ifUserSignedIn()
                .switchMap(user -> observableProvider.call(user.getUid()));
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

    public Completable signOut() {
        auth.signOut();
        return signInAnonymously();
    }

    public Completable signInAnonymously() {
        return Completable.create(e -> auth.signInAnonymously()
                .addOnSuccessListener(result -> e.onComplete())
                .addOnFailureListener(e::onError));
    }

    private interface TaskProvider<T> {

        Task<T> task();
    }
}
