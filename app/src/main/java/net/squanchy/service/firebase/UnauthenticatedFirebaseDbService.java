package net.squanchy.service.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseRoot;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class UnauthenticatedFirebaseDbService implements FirebaseDbService {

    private final DatabaseReference database;

    public UnauthenticatedFirebaseDbService(DatabaseReference database) {
        this.database = database;
    }

    @Override
    public Observable<FirebaseDays> days() {
        return observeChild("newmodel2/days", FirebaseDays.class);
    }

    @Override
    public Observable<FirebaseSpeakers> speakers() {
        return observeChild("newmodel2/speakers", FirebaseSpeakers.class);
    }

    @Override
    public Observable<FirebaseSchedule> sessions() {
        return observeChild("newmodel2/events", FirebaseSchedule.class);
    }

    @Override
    public Observable<FirebaseEvent> event(int dayId, int eventId) {
        String path = String.format(Locale.US, "newmodel/events/%2$d", dayId, eventId);
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

    private Observable<FirebaseRoot> observeRoot() {
        return Observable.create((ObservableEmitter<FirebaseRoot> e) -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseRoot value = dataSnapshot.getValue(FirebaseRoot.class);
                    e.onNext(value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    e.onError(databaseError.toException());
                }
            };

            database.child("newmodel").addValueEventListener(listener);
            e.setCancellable(() -> database.removeEventListener(listener));
        }).observeOn(Schedulers.io());
    }
}
