package net.squanchy.service.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseEvents;
import net.squanchy.service.firebase.model.FirebaseFavorites;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public final class AuthenticatedFirebaseDbService implements FirebaseDbService {

    private static final String DAYS_NODE = "days";
    private static final String SPEAKERS_NODE = "speakers";
    private static final String EVENTS_NODE = "events";
    private static final String EVENTS_BY_ID_NODE = "events/events/%1$s";
    private static final String FAVORITES_NODE = "user/%1$s/favorites";

    private final DatabaseReference database;
    private final FirebaseAuthService authService;

    public AuthenticatedFirebaseDbService(DatabaseReference database, FirebaseAuthService authService) {
        this.database = database;
        this.authService = authService;
    }

    @Override
    public Observable<FirebaseDays> days() {
        return authService.signInAnd(userId -> observeChild(DAYS_NODE, FirebaseDays.class));
    }

    @Override
    public Observable<FirebaseSpeakers> speakers() {
        return authService.signInAnd(userId -> observeChild(SPEAKERS_NODE, FirebaseSpeakers.class));
    }

    @Override
    public Observable<FirebaseEvents> events() {
        return authService.signInAnd(userId -> observeChild(EVENTS_NODE, FirebaseEvents.class));
    }

    @Override
    public Observable<FirebaseEvent> event(String eventId) {
        String path = String.format(Locale.US, EVENTS_BY_ID_NODE, eventId);
        return authService.signInAnd(userId -> observeChild(path, FirebaseEvent.class));
    }

    @Override
    public Observable<FirebaseFavorites> favorites() {
        return authService.signInAnd(userId -> observeChild(String.format(Locale.US, FAVORITES_NODE, userId), FirebaseFavorites.class));
    }

    private <T> Observable<T> observeChild(final String path, final Class<T> clazz) {
        return Observable.create((ObservableEmitter<T> e) -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    T value = dataSnapshot.getValue(clazz);
                    e.onNext(value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    e.onError(databaseError.toException());
                }
            };

            database.child(path).addValueEventListener(listener);
            e.setCancellable(() -> database.removeEventListener(listener));
        }).observeOn(Schedulers.io());
    }
}
