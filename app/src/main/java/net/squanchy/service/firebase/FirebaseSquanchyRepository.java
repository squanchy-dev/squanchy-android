package net.squanchy.service.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseFloorPlans;
import net.squanchy.service.firebase.model.FirebaseInfoItems;
import net.squanchy.service.firebase.model.FirebaseLevels;
import net.squanchy.service.firebase.model.FirebaseLocations;
import net.squanchy.service.firebase.model.FirebasePois;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSettings;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.service.firebase.model.FirebaseTracks;
import net.squanchy.service.firebase.model.FirebaseTypes;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public final class FirebaseSquanchyRepository {

    private final DatabaseReference database;

    public FirebaseSquanchyRepository(DatabaseReference database) {
        this.database = database;
    }

    public Observable<FirebaseSettings> settings() {
        return observeChild("settings", FirebaseSettings.class);
    }

    public Observable<FirebaseFloorPlans> floorPlans() {
        return observeChild("floorPlans", FirebaseFloorPlans.class);
    }

    public Observable<FirebaseInfoItems> info() {
        return observeChild("info", FirebaseInfoItems.class);
    }

    public Observable<FirebaseLevels> levels() {
        return observeChild("levels", FirebaseLevels.class);
    }

    public Observable<FirebaseLocations> locations() {
        return observeChild("locations", FirebaseLocations.class);
    }

    public Observable<FirebasePois> pois() {
        return observeChild("pois", FirebasePois.class);
    }

    public Observable<FirebaseSpeakers> speakers() {
        return observeChild("speakers", FirebaseSpeakers.class);
    }

    public Observable<FirebaseTracks> tracks() {
        return observeChild("tracks", FirebaseTracks.class);
    }

    public Observable<FirebaseTypes> types() {
        return observeChild("types", FirebaseTypes.class);
    }

    public Observable<FirebaseSchedule> socialEvents() {
        return observeChild("socialEvents", FirebaseSchedule.class);
    }

    public Observable<FirebaseSchedule> sessions() {
        return observeChild("sessions", FirebaseSchedule.class);
    }

    public Observable<FirebaseEvent> event(int dayId, int eventId) {
        return observeChild(String.format("sessions/days/%1$d/events/%2$d", dayId, eventId), FirebaseEvent.class);
    }

    private <T> Observable<T> observeChild(final String path, final Class<T> clazz) {
        return Observable.create((ObservableEmitter<T> e) -> {
            database.child(path).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    T value = dataSnapshot.getValue(clazz);
                    e.onNext(value);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    e.onError(databaseError.toException());
                }
            });
        }).observeOn(Schedulers.io());
    }
}
