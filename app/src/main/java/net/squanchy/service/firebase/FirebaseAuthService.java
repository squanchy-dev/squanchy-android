package net.squanchy.service.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class FirebaseAuthService {

    private final FirebaseAuth auth;

    public FirebaseAuthService(FirebaseAuth auth) {
        this.auth = auth;
    }

    Observable<Optional<FirebaseUser>> currentUser() {
        return Observable.create(e -> {
            FirebaseAuth.AuthStateListener listener = firebaseAuth -> e.onNext(Optional.fromNullable(firebaseAuth.getCurrentUser()));

            auth.addAuthStateListener(listener);

            e.setCancellable(() -> auth.removeAuthStateListener(listener));
        });
    }

    Completable signInAnonymously() {
        return Completable.create(e -> {
            auth.signInAnonymously()
                    .addOnSuccessListener(result -> e.onComplete())
                    .addOnFailureListener(e::onError);
        });
    }
}
