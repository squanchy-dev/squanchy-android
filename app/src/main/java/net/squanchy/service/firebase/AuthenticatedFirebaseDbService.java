package net.squanchy.service.firebase;

import java.util.concurrent.Callable;

import net.squanchy.service.firebase.model.FirebaseEvent;

import io.reactivex.Observable;

public class AuthenticatedFirebaseDbService implements FirebaseDbService {

    private final FirebaseDbService dbService;
    private final FirebaseAuthService authService;

    public AuthenticatedFirebaseDbService(FirebaseDbService dbService, FirebaseAuthService authService) {
        this.dbService = dbService;
        this.authService = authService;
    }

    @Override
    public Observable<FirebaseInfoItems> info() {
        return signInAnd(dbService::info);
    }

    @Override
    public Observable<FirebaseSpeakers> speakers() {
        return signInAnd(dbService::speakers);
    }

    @Override
    public Observable<FirebaseSchedule> sessions() {
        return signInAnd(dbService::sessions);
    }

    @Override
    public Observable<FirebaseEvent> event(int dayId, int eventId) {
        return signInAnd(() -> dbService.event(dayId, eventId));
    }

    private <T> Observable<T> signInAnd(Callable<Observable<T>> andThen) {
        return authService.currentUser().flatMap(user -> {
            if (user.isPresent()) {
                return andThen.call();
            }

            return authService.signInAnonymously().andThen(Observable.empty());
        });
    }
}
