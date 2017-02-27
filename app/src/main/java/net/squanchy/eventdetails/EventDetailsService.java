package net.squanchy.eventdetails;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.support.lang.Lists;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

class EventDetailsService {

    private final FirebaseDbService dbService;

    EventDetailsService(FirebaseDbService dbService) {
        this.dbService = dbService;
    }

    public Observable<Event> event(int dayId, int eventId) {
        Observable<FirebaseEvent> eventObservable = dbService.event(dayId, eventId);
        Observable<FirebaseSpeakers> speakersObservable = dbService.speakers();

        return Observable.combineLatest(
                eventObservable,
                speakersObservable,
                combineIntoEvent(dayId)
        ).subscribeOn(Schedulers.io());
    }

    private BiFunction<FirebaseEvent, FirebaseSpeakers, Event> combineIntoEvent(int dayId) {
        return (apiEvent, apiSpeakers) -> {
            List<FirebaseSpeaker> speakers = speakersForEvent(apiEvent, apiSpeakers);
            return Event.create(
                    apiEvent.eventId,
                    dayId,
                    apiEvent.name,
                    apiEvent.place,
                    ExperienceLevel.fromRawLevel(apiEvent.experienceLevel - 1), // TODO fix the data
                    map(speakers, toSpeakerName())
            );
        };
    }

    private List<FirebaseSpeaker> speakersForEvent(FirebaseEvent apiEvent, FirebaseSpeakers apiSpeakers) {
        return map(apiEvent.speakers, speakerId -> findSpeaker(apiSpeakers, speakerId));
    }

    private FirebaseSpeaker findSpeaker(FirebaseSpeakers apiSpeakers, long speakerId) {
        return find(apiSpeakers.speakers, apiSpeaker -> apiSpeaker.speakerId.equals(speakerId));
    }

    private Lists.Function<FirebaseSpeaker, String> toSpeakerName() {
        return apiSpeaker -> apiSpeaker != null ? apiSpeaker.firstName + " " + apiSpeaker.lastName : null;
    }
}
