package net.squanchy.service.firebase;

import com.google.android.gms.tasks.Task;
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
import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public final class AuthenticatedFirebaseDbService implements FirebaseDbService {

    private static final String DAYS_NODE = "days";
    private static final String SPEAKERS_NODE = "speakers";
    private static final String EVENTS_NODE = "events";
    private static final String EVENTS_BY_ID_NODE = "events/events/%1$s";
    private static final String FAVORITES_NODE = "user/%1$s/";
    private static final String FAVORITES_BY_ID_NODE = "user/%1$s/favorites/%2$s";

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
        return authService.signInAnd(userId -> {
            String path = String.format(Locale.US, FAVORITES_NODE, userId);
            return observeOptionalChild(path, FirebaseFavorites.class)
                    .map(optionalFavorites -> optionalFavorites.or(FirebaseFavorites.empty()));
        });
    }

    private <T> Observable<T> observeChild(final String path, final Class<T> clazz) {
        return observeChildAndEmit(path, clazz, Emitter::onNext);
    }

    private <T> Observable<Optional<T>> observeOptionalChild(final String path, final Class<T> clazz) {
        return observeChildAndEmit(path, clazz, (emitter, value) -> emitter.onNext(Optional.fromNullable(value)));
    }

    private <T, V> Observable<T> observeChildAndEmit(String path, final Class<V> clazz, NextEmitter<T, V> nextEmitter) {
        return Observable.create((ObservableEmitter<T> e) -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    V value = dataSnapshot.getValue(clazz);
                    nextEmitter.emit(e, value);
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

    @Override
    public Completable favorite(String eventId) {
        return updateFavorite(eventId, reference -> reference.setValue(true));
    }

    @Override
    public Completable removeFavorite(String eventId) {
        return updateFavorite(eventId, DatabaseReference::removeValue);
    }

    private Completable updateFavorite(String eventId, Func1<DatabaseReference, Task<Void>> action) {
        return authService.signInAnd(Observable::just)
                .flatMapCompletable(userId -> Completable.create(emitter -> {
                    String node = String.format(Locale.US, FAVORITES_BY_ID_NODE, userId, eventId);
                    action.call(database.child(node))
                            .addOnSuccessListener(result -> emitter.onComplete())
                            .addOnFailureListener(emitter::onError);
                }));
    }

    private interface NextEmitter<T, V> {
        void emit(ObservableEmitter<T> emitter, V value);
    }
}
