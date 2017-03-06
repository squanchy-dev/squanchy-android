package net.squanchy.service.view;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseEvents;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.support.lang.Checksum;
import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.Optional;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

import static net.squanchy.support.lang.Lists.filter;
import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

public class EventService {

    private final FirebaseDbService dbService;
    private final Checksum checksum;

    public EventService(FirebaseDbService dbService, Checksum checksum) {
        this.dbService = dbService;
        this.checksum = checksum;
    }

    public Observable<List<Event>> events() {
        Observable<FirebaseEvents> sessionsObservable = dbService.events();
        Observable<FirebaseSpeakers> speakersObservable = dbService.speakers();

        return Observable.combineLatest(sessionsObservable, speakersObservable, combineSessionsAndSpeakers());
    }

    private BiFunction<FirebaseEvents, FirebaseSpeakers, List<Event>> combineSessionsAndSpeakers() {
        return (apiSchedule, apiSpeakers) -> Lists.map(apiSchedule.events, combineEventWith(apiSpeakers));
    }

    private Func1<FirebaseEvent, Event> combineEventWith(FirebaseSpeakers apiSpeakers) {
        return apiEvent -> {
            List<FirebaseSpeaker> speakers = speakersForEvent(apiEvent, apiSpeakers);

            return Event.create(
                    apiEvent.id,
                    checksum.getChecksumOf(apiEvent.id),
                    apiEvent.day_id,
                    apiEvent.name,
                    apiEvent.place_id,
                    Optional.fromNullable(apiEvent.experience_level).flatMap(ExperienceLevel::fromNullableRawLevel),
                    map(speakers, toSpeakerName()));
        };
    }

    private List<FirebaseSpeaker> speakersForEvent(FirebaseEvent apiEvent, FirebaseSpeakers apiSpeakers) {
        List<Optional<FirebaseSpeaker>> speakers = map(apiEvent.speaker_ids, speakerId -> findSpeaker(apiSpeakers, speakerId));
        List<Optional<FirebaseSpeaker>> presentSpeakers = filter(speakers, Optional::isPresent);
        return map(presentSpeakers, Optional::get);
    }

    private Optional<FirebaseSpeaker> findSpeaker(FirebaseSpeakers apiSpeakers, String speakerId) {
        return find(apiSpeakers.speakers, apiSpeaker -> apiSpeaker.id.equals(speakerId));
    }

    private Func1<FirebaseSpeaker, String> toSpeakerName() {
        return apiSpeaker -> apiSpeaker != null ? apiSpeaker.name : null;
    }
}
