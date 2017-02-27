package net.squanchy.schedule;

import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseDay;
import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.support.lang.Ids;
import net.squanchy.support.lang.Lists;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Ids.*;
import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

class ScheduleService {

    private final FirebaseDbService dbService;

    ScheduleService(FirebaseDbService dbService) {
        this.dbService = dbService;
    }

    public Observable<Schedule> schedule() {
        Observable<FirebaseSchedule> sessionsObservable = dbService.sessions();
        Observable<FirebaseSpeakers> speakersObservable = dbService.speakers();
        Observable<FirebaseDays> daysObservable = dbService.days();

        return Observable.combineLatest(
                sessionsObservable,
                speakersObservable,
                combineIntoSchedule()
        ).subscribeOn(Schedulers.io());
    }

    private BiFunction<FirebaseSchedule, FirebaseSpeakers, Schedule> combineIntoSchedule() {
        return (apiSchedule, apiSpeakers) -> {
            List<SchedulePage> pages = map(apiSchedule.sessions., toSchedulePage(apiSchedule, apiSpeakers));
            return Schedule.create(pages);
        };
    }

    private Lists.Function<FirebaseDay, SchedulePage> toSchedulePage(FirebaseSchedule apiSchedule, FirebaseSpeakers apiSpeakers) {
        return apiDay -> {
            int dayId = apiSchedule.sessions;
            return SchedulePage.create(
                    apiDay.date,
                    map(apiDay.events, toEvent(apiSpeakers, dayId))
            );
        };
    }

    private Lists.Function<FirebaseEvent, Event> toEvent(FirebaseSpeakers apiSpeakers, int dayId) {
        return apiEvent -> {
            List<FirebaseSpeaker> speakers = speakersForEvent(apiEvent, apiSpeakers);
            return Event.create(
                    safelyConvertId(apiEvent.id),
                    dayId,      // TODO do this less crappily
                    apiEvent.name,
                    apiEvent.place_id,
                    ExperienceLevel.fromRawLevel(apiEvent.experience_level), // TODO fix the data
                    map(speakers, toSpeakerName()));
        };
    }

    private List<FirebaseSpeaker> speakersForEvent(FirebaseEvent apiEvent, FirebaseSpeakers apiSpeakers) {
        return map(apiEvent.speaker_ids, speakerId -> findSpeaker(apiSpeakers, speakerId));
    }

    private FirebaseSpeaker findSpeaker(FirebaseSpeakers apiSpeakers, String speakerId) {
        return find(apiSpeakers.speakers, apiSpeaker -> apiSpeaker.id.equals(speakerId));
    }

    private Lists.Function<FirebaseSpeaker, String> toSpeakerName() {
        return apiSpeaker -> apiSpeaker != null ? apiSpeaker.name : null;
    }
}
