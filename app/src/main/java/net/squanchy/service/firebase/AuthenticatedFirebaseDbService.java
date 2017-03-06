package net.squanchy.service.firebase;

import java.util.concurrent.Callable;

import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseEvents;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Observable;

public class AuthenticatedFirebaseDbService implements FirebaseDbService {

    private final FirebaseDbService dbService;
    private final FirebaseAuthService authService;

    public AuthenticatedFirebaseDbService(FirebaseDbService dbService, FirebaseAuthService authService) {
        this.dbService = dbService;
        this.authService = authService;
    }

    @Override
    public Observable<FirebaseDays> days() {
        return signInAnd(dbService::days);
    }

    @Override
    public Observable<FirebaseSpeakers> speakers() {
        return signInAnd(dbService::speakers);
    }

    @Override
    public Observable<FirebaseEvents> events() {
        return signInAnd(dbService::events);
    }

    @Override
    public Observable<FirebaseEvent> event(String dayId, String eventId) {
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
