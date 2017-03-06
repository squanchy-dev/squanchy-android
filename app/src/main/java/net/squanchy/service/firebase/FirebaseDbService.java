package net.squanchy.service.firebase;

import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseEvents;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Observable;

public interface FirebaseDbService {

    Observable<FirebaseDays> days();

    Observable<FirebaseSpeakers> speakers();

    Observable<FirebaseEvents> events();

    Observable<FirebaseEvent> event(String dayId, String eventId);
}
