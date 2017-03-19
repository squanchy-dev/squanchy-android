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
import net.squanchy.service.firebase.model.FirebasePlace;
import net.squanchy.service.firebase.model.FirebasePlaces;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.service.firebase.model.FirebaseTrack;
import net.squanchy.service.firebase.model.FirebaseTracks;
import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Optional;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public final class FirebaseDbService {

    private static final String DAYS_NODE = "data/days";
    private static final String SPEAKERS_NODE = "data/speakers";
    private static final String EVENTS_NODE = "data/events";
    private static final String EVENTS_BY_ID_NODE = "data/events/events/%1$s";
    private static final String PLACES_NODE = "data/places";
    private static final String PLACES_BY_ID_NODE = "data/places/%1$s";
    private static final String TRACKS_NODE = "data/tracks";
    private static final String TRACKS_BY_ID_NODE = "data/tracks/%1$s";
    private static final String FAVORITES_NODE = "user/%1$s/";
    private static final String FAVORITES_BY_ID_NODE = "user/%1$s/favorites/%2$s";

    private final DatabaseReference database;

    public FirebaseDbService(DatabaseReference database) {
        this.database = database;
    }

    public Observable<FirebaseDays> days() {
        return observeChild(DAYS_NODE, FirebaseDays.class);
    }

    public Observable<FirebaseSpeakers> speakers() {
        return observeChild(SPEAKERS_NODE, FirebaseSpeakers.class);
    }

    public Observable<FirebaseEvents> events() {
        return observeChild(EVENTS_NODE, FirebaseEvents.class);
    }

    public Observable<FirebaseEvent> event(String eventId) {
        String path = String.format(Locale.US, EVENTS_BY_ID_NODE, eventId);
        return observeChild(path, FirebaseEvent.class);
    }

    public Observable<FirebasePlaces> places() {
        return observeChild(PLACES_NODE, FirebasePlaces.class);
    }

    public Observable<FirebasePlace> place(String placeId) {
        String path = String.format(PLACES_BY_ID_NODE, placeId);
        return observeChild(path, FirebasePlace.class);
    }

    public Observable<FirebaseTracks> tracks() {
        return observeChild(TRACKS_NODE, FirebaseTracks.class);
    }

    public Observable<FirebaseTrack> track(String trackId) {
        String path = String.format(TRACKS_BY_ID_NODE, trackId);
        return observeChild(path, FirebaseTrack.class);
    }

    public Observable<FirebaseFavorites> favorites(String userId) {
        String path = String.format(Locale.US, FAVORITES_NODE, userId);

        return observeOptionalChild(path, FirebaseFavorites.class)
                .map(optionalFavorites -> optionalFavorites.or(FirebaseFavorites.empty()));
    }

    private <T> Observable<T> observeChild(final String path, final Class<T> clazz) {
        return observeChildAndEmit(path, clazz, value -> value);
    }

    private <T> Observable<Optional<T>> observeOptionalChild(final String path, final Class<T> clazz) {
        return observeChildAndEmit(path, clazz, Optional::fromNullable);
    }

    private <T, V> Observable<T> observeChildAndEmit(String path, final Class<V> clazz, Func1<V, T> valueMapper) {
        return Observable.create((ObservableEmitter<T> e) -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    V value = dataSnapshot.getValue(clazz);
                    if (e.isDisposed()) {
                        return;
                    }
                    e.onNext(valueMapper.call(value));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (e.isDisposed()) {
                        return;
                    }
                    e.onError(databaseError.toException());
                }
            };

            DatabaseReference childReference = database.child(path);
            childReference.addValueEventListener(listener);
            e.setCancellable(() -> childReference.removeEventListener(listener));
        }).observeOn(Schedulers.io());
    }

    public Completable addFavorite(String eventId, String userId) {
        return updateFavorite(eventId, reference -> reference.setValue(true), userId);
    }

    public Completable removeFavorite(String eventId, String userId) {
        return updateFavorite(eventId, DatabaseReference::removeValue, userId);
    }

    private Completable updateFavorite(String eventId, Func1<DatabaseReference, Task<Void>> action, String userId) {
        return Completable.create(emitter -> {
            String path = String.format(Locale.US, FAVORITES_BY_ID_NODE, userId, eventId);
            action.call(database.child(path))
                    .addOnSuccessListener(result -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }
}
