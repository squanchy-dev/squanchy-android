package net.squanchy.eventdetails;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.support.lang.Checksum;
import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Optional;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

class EventDetailsService {

    private final FirebaseDbService dbService;
    private final Checksum checksum;

    EventDetailsService(FirebaseDbService dbService, Checksum checksum) {
        this.dbService = dbService;
        this.checksum = checksum;
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
                    apiEvent.id,
                    checksum.getChecksumOf(apiEvent.id),
                    dayId,
                    apiEvent.name,
                    apiEvent.place_id,
                    Optional.fromNullable(apiEvent.experience_level).flatMap(ExperienceLevel::fromNullableRawLevel),
                    map(speakers, toSpeakerName())
            );
        };
    }

    private List<FirebaseSpeaker> speakersForEvent(FirebaseEvent apiEvent, FirebaseSpeakers apiSpeakers) {
        return map(apiEvent.speaker_ids, speakerId -> findSpeaker(apiSpeakers, speakerId));
    }

    private FirebaseSpeaker findSpeaker(FirebaseSpeakers apiSpeakers, String speakerId) {
        return find(apiSpeakers.speakers, apiSpeaker -> apiSpeaker.id.equals(speakerId));
    }

    private Func1<FirebaseSpeaker, String> toSpeakerName() {
        return apiSpeaker -> apiSpeaker != null ? apiSpeaker.name : null;
    }
}
