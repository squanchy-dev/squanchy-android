package net.squanchy.eventdetails;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseSquanchyRepository;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSpeaker;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

class EventDetailsService {

    private final FirebaseSquanchyRepository repository;

    EventDetailsService(FirebaseSquanchyRepository repository) {
        this.repository = repository;
    }

    public Observable<Event> event(int dayId, int eventId) {
        Observable<FirebaseEvent> eventObservable = repository.event(dayId, eventId);
        Observable<FirebaseSpeaker.Holder> speakersObservable = repository.speakers();

        return Observable.combineLatest(
                eventObservable,
                speakersObservable,
                (firebaseEvent, speakerHolder) -> Event.create(
                        firebaseEvent.eventId,
                        dayId,
                        firebaseEvent.name,
                        firebaseEvent.place,
                        firebaseEvent.experienceLevel,
                        map(
                                map(firebaseEvent.speakers, speakerId -> find(
                                        speakerHolder.speakers, speaker -> speaker.speakerId.equals(speakerId))
                                ),
                                speaker -> speaker == null ? "" : speaker.firstName + " " + speaker.lastName
                        )
                )
        ).subscribeOn(Schedulers.io());
    }
}
