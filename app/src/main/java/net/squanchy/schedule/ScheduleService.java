package net.squanchy.schedule;

import java.util.Date;
import java.util.List;

import net.squanchy.eventdetails.domain.view.ExperienceLevel;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseDay;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Lists;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

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

        return Observable.combineLatest(
                sessionsObservable,
                speakersObservable,
                combineIntoSchedule()
        ).subscribeOn(Schedulers.io());
    }

    private BiFunction<FirebaseSchedule, FirebaseSpeakers, Schedule> combineIntoSchedule() {
        return (apiSchedule, apiSpeakers) -> {
            List<SchedulePage> pages = map(apiSchedule.days, toSchedulePage(apiSchedule, apiSpeakers));
            return Schedule.create(pages);
        };
    }

    private Lists.Function<FirebaseDay, SchedulePage> toSchedulePage(FirebaseSchedule apiSchedule, FirebaseSpeakers apiSpeakers) {
        return apiDay -> {
            int dayId = apiSchedule.days.indexOf(apiDay);
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
                    apiEvent.eventId,
                    dayId,      // TODO do this less crappily
                    new Date(), // TODO: use real date
                    new Date(), // TODO: use real date
                    apiEvent.name,
                    apiEvent.place,
                    ExperienceLevel.fromRawLevel(apiEvent.experienceLevel - 1), // TODO fix the data
                    map(speakers, toSpeaker())
            );
        };
    }

    private List<FirebaseSpeaker> speakersForEvent(FirebaseEvent apiEvent, FirebaseSpeakers apiSpeakers) {
        return map(apiEvent.speakers, speakerId -> findSpeaker(apiSpeakers, speakerId));
    }

    private FirebaseSpeaker findSpeaker(FirebaseSpeakers apiSpeakers, long speakerId) {
        return find(apiSpeakers.speakers, apiSpeaker -> apiSpeaker.speakerId.equals(speakerId));
    }

    private Lists.Function<FirebaseSpeaker, Speaker> toSpeaker() {
        return apiSpeaker -> apiSpeaker != null ? Speaker.create(apiSpeaker) : null;
    }
}
