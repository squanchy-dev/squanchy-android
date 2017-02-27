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
import io.reactivex.functions.Function3;
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
                daysObservable,
                combineIntoSchedule()
        ).subscribeOn(Schedulers.io());
    }

    private Function3<FirebaseSchedule, FirebaseSpeakers, FirebaseDays, Schedule> combineIntoSchedule() {
        return (apiSchedule, apiSpeakers, apiDays) -> {
            List<SchedulePage> pages = map(apiSchedule.sessions, toSchedulePage(apiSchedule, apiSpeakers, apiDays));
            return Schedule.create(pages);
        };
    }

    private Lists.Function<FirebaseEvent, SchedulePage> toSchedulePage(FirebaseSchedule apiSchedule, FirebaseSpeakers apiSpeakers, FirebaseDays apiDays) {
        return firebaseEvent -> {
            int dayId = Ids.safelyConvertIdToInt(firebaseEvent.day_id);
            String date = findDate(apiDays, firebaseEvent.day_id);
            return SchedulePage.create(date, map(apiSchedule.sessions,toEvent(apiSpeakers, dayId)));
        };
    }

    private String findDate(FirebaseDays apiDays, String day_id) {
        return find(apiDays.days, firebaseDay -> firebaseDay.id.equals(day_id)).date;
    }

    private Lists.Function<FirebaseEvent, Event> toEvent(FirebaseSpeakers apiSpeakers, int dayId) {
        return apiEvent -> {
            List<FirebaseSpeaker> speakers = speakersForEvent(apiEvent, apiSpeakers);
            return Event.create(
                    safelyConvertIdToLong(apiEvent.id),
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
