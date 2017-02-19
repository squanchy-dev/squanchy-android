package net.squanchy.service.firebase;

import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseInfoItems;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Observable;

public interface FirebaseDbService {

    Observable<FirebaseInfoItems> info();

    Observable<FirebaseSpeakers> speakers();

    Observable<FirebaseSchedule> sessions();

    Observable<FirebaseEvent> event(int dayId, int eventId);
}
