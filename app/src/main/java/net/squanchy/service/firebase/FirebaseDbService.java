package net.squanchy.service.firebase;

import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseEvents;
import net.squanchy.service.firebase.model.FirebaseFavorites;
import net.squanchy.service.firebase.model.FirebaseSpeakers;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface FirebaseDbService {

    Observable<FirebaseDays> days();

    Observable<FirebaseSpeakers> speakers();

    Observable<FirebaseEvents> events();

    Observable<FirebaseEvent> event(String eventId);

    Observable<FirebaseFavorites> favorites(String userId);

    Completable addFavorite(String eventId, String userId);

    Completable removeFavorite(String eventId, String userId);
}
