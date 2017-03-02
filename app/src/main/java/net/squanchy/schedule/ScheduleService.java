package net.squanchy.schedule;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.support.lang.Checksum;
import net.squanchy.support.lang.Func1;
import net.squanchy.support.lang.Func2;
import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.OptionalEnums;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Ids.safelyConvertIdToInt;
import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

class ScheduleService {

    private final FirebaseDbService dbService;
    private final Checksum checksum;

    ScheduleService(FirebaseDbService dbService, Checksum checksum) {
        this.dbService = dbService;
        this.checksum = checksum;
    }

    public Observable<Schedule> schedule() {
        Observable<FirebaseSchedule> sessionsObservable = dbService.sessions();
        Observable<FirebaseSpeakers> speakersObservable = dbService.speakers();
        final Observable<FirebaseDays> daysObservable = dbService.days();

        return Observable.combineLatest(sessionsObservable, speakersObservable, combineSessionsAndSpeakers())
                .map(mapEventsToDays())
                .withLatestFrom(daysObservable, combineSessionsById())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    private BiFunction<FirebaseSchedule, FirebaseSpeakers, List<Event>> combineSessionsAndSpeakers() {
        return (apiSchedule, apiSpeakers) -> Lists.map(apiSchedule.events, combineEventWith(apiSpeakers));
    }

    private Func1<FirebaseEvent, Event> combineEventWith(FirebaseSpeakers apiSpeakers) {
        return apiEvent -> {
            List<FirebaseSpeaker> speakers = speakersForEvent(apiEvent, apiSpeakers);

            return Event.create(
                    apiEvent.id,
                    checksum.getChecksumOf(apiEvent.id),
                    safelyConvertIdToInt(apiEvent.day_id),
                    apiEvent.name,
                    apiEvent.place_id,
                    OptionalEnums.from(apiEvent.experience_level),
                    map(speakers, toSpeakerName()));
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

    @NonNull
    private Function<List<Event>, HashMap<Integer, List<Event>>> mapEventsToDays() {
        return events -> Lists.reduce(new HashMap<>(), events, listToDaysHashMap());
    }

    @NonNull
    private Func2<HashMap<Integer, List<Event>>, Event, HashMap<Integer, List<Event>>> listToDaysHashMap() {
        return (map, event) -> {
            List<Event> dayList = getOrCreateDayList(map, event);
            dayList.add(event);
            map.put(event.day(), dayList);
            return map;
        };
    }

    private List<Event> getOrCreateDayList(HashMap<Integer, List<Event>> map, Event event) {
        List<Event> currentList = map.get(event.day());

        if (currentList == null) {
            currentList = new ArrayList<>();
            map.put(event.day(), currentList);
        }

        return currentList;
    }

    @NonNull
    private BiFunction<HashMap<Integer, List<Event>>, FirebaseDays, Schedule> combineSessionsById() {
        return (map, apiDays) -> {
            List<SchedulePage> pages = new ArrayList<>(map.size());
            for (Integer dayId : map.keySet()) {
                String date = findDate(apiDays, dayId);
                pages.add(SchedulePage.create(date, map.get(dayId)));
            }

            return Schedule.create(pages);
        };
    }

    private String findDate(FirebaseDays apiDays, int dayId) {
        return find(apiDays.days, firebaseDay -> firebaseDay.id.equals(String.valueOf(dayId))).date;
    }
}
