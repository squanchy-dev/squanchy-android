package net.squanchy.service.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseInfoItems;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public final class UnauthenticatedFirebaseDbService implements FirebaseDbService {

    private final DatabaseReference database;

    public UnauthenticatedFirebaseDbService(DatabaseReference database) {
        this.database = database;
    }

    @Override
    public Observable<FirebaseInfoItems> info() {
        return observeChild("info", FirebaseInfoItems.class);
    }

    @Override
    public Observable<FirebaseSpeakers> speakers() {
        return observeChild("speakers", FirebaseSpeakers.class);
    }

    @Override
    public Observable<FirebaseSchedule> sessions() {
        return observeChild("sessions", FirebaseSchedule.class);
    }

    @Override
    public Observable<FirebaseEvent> event(int dayId, int eventId) {
        return observeChild(String.format("sessions/days/%1$d/events/%2$d", dayId, eventId), FirebaseEvent.class);
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
