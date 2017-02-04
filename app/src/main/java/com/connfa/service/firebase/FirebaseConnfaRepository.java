package com.connfa.service.firebase;

import com.connfa.service.firebase.model.FirebaseEvent;
import com.connfa.service.firebase.model.FirebaseFloorPlan;
import com.connfa.service.firebase.model.FirebaseInfoItem;
import com.connfa.service.firebase.model.FirebaseLevel;
import com.connfa.service.firebase.model.FirebaseLocation;
import com.connfa.service.firebase.model.FirebasePoi;
import com.connfa.service.firebase.model.FirebaseSettings;
import com.connfa.service.firebase.model.FirebaseSpeaker;
import com.connfa.service.firebase.model.FirebaseTrack;
import com.connfa.service.firebase.model.FirebaseType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public final class FirebaseConnfaRepository {

    private final DatabaseReference database;

    public FirebaseConnfaRepository(DatabaseReference database) {
        this.database = database;
    }

    public Observable<FirebaseSettings.Holder> settings() {
        return observeChild("settings", FirebaseSettings.Holder.class);
    }

    public Observable<FirebaseFloorPlan.Holder> floorPlans() {
        return observeChild("floorPlans", FirebaseFloorPlan.Holder.class);
    }

    public Observable<FirebaseInfoItem.General> info() {
        return observeChild("info", FirebaseInfoItem.General.class);
    }

    public Observable<FirebaseLevel.Holder> levels() {
        return observeChild("levels", FirebaseLevel.Holder.class);
    }

    public Observable<FirebaseLocation.Holder> locations() {
        return observeChild("locations", FirebaseLocation.Holder.class);
    }

    public Observable<FirebasePoi.Holder> pois() {
        return observeChild("pois", FirebasePoi.Holder.class);
    }

    public Observable<FirebaseSpeaker.Holder> speakers() {
        return observeChild("speakers", FirebaseSpeaker.Holder.class);
    }

    public Observable<FirebaseTrack.Holder> tracks() {
        return observeChild("tracks", FirebaseTrack.Holder.class);
    }

    public Observable<FirebaseType.Holder> types() {
        return observeChild("types", FirebaseType.Holder.class);
    }

    public Observable<FirebaseEvent.Holder> socialEvents() {
        return observeChild("socialEvents", FirebaseEvent.Holder.class);
    }

    public Observable<FirebaseEvent.Holder> sessions() {
        return observeChild("sessions", FirebaseEvent.Holder.class);
    }

    private <T> Observable<T> observeChild(final String path, final Class<T> clazz) {
        return Observable.create((ObservableEmitter<T> e) -> {
            database.child(path).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final T value = dataSnapshot.getValue(clazz);
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
