package net.squanchy.service.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import net.squanchy.service.firebase.model.FirebaseDay;
import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseEvents;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class UnauthenticatedFirebaseDbService implements FirebaseDbService {

    private static final String DAYS_NODE = "days";
    private static final String SPEAKERS_NODE = "speakers";
    private static final String EVENTS_NODE = "events";
    private static final String EVENTS_BY_ID_NODE = "events/events/%1$s";

    private final DatabaseReference database;

    public UnauthenticatedFirebaseDbService(DatabaseReference database) {
        this.database = database;
    }

    @Override
    public Observable<FirebaseDays> days() {
        return observeChild(DAYS_NODE, FirebaseDays.class)
                .map(sortByPosition());
    }

    private Function<FirebaseDays, FirebaseDays> sortByPosition() {
        return firebaseDays -> {
            ArrayList<FirebaseDay> sortedFirebaseDays = new ArrayList<>(firebaseDays.days);
            Collections.sort(sortedFirebaseDays, (firstDay, secondDay) -> Integer.compare(firstDay.position, secondDay.position));
            return new FirebaseDays(sortedFirebaseDays);
        };
    }

    @Override
    public Observable<FirebaseSpeakers> speakers() {
        return observeChild(SPEAKERS_NODE, FirebaseSpeakers.class);
    }

    @Override
    public Observable<FirebaseEvents> events() {
        return observeChild(EVENTS_NODE, FirebaseEvents.class);
    }

    @Override
    public Observable<FirebaseEvent> event(String eventId) {
        String path = String.format(Locale.US, EVENTS_BY_ID_NODE, eventId);
        return observeChild(path, FirebaseEvent.class);
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
