package net.squanchy.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseDays;
import net.squanchy.service.repository.EventRepository;
import net.squanchy.support.lang.Func2;
import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.Optional;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.find;

class ScheduleService {

    private final FirebaseDbService dbService;
    private final EventRepository eventRepository;

    ScheduleService(FirebaseDbService dbService, EventRepository eventRepository) {
        this.dbService = dbService;
        this.eventRepository = eventRepository;
    }

    public Observable<Schedule> schedule() {
        final Observable<FirebaseDays> daysObservable = dbService.days();

        return eventRepository.events()
                .map(mapEventsToDays())
                .withLatestFrom(daysObservable, combineSessionsById())
                .subscribeOn(Schedulers.io());
    }

    private Function<List<Event>, HashMap<String, List<Event>>> mapEventsToDays() {
        return events -> Lists.reduce(new HashMap<>(), events, listToDaysHashMap());
    }

    private Func2<HashMap<String, List<Event>>, Event, HashMap<String, List<Event>>> listToDaysHashMap() {
        return (map, event) -> {
            List<Event> dayList = getOrCreateDayList(map, event);
            dayList.add(event);
            map.put(event.dayId(), dayList);
            return map;
        };
    }

    private List<Event> getOrCreateDayList(HashMap<String, List<Event>> map, Event event) {
        List<Event> currentList = map.get(event.dayId());

        if (currentList == null) {
            currentList = new ArrayList<>();
            map.put(event.dayId(), currentList);
        }

        return currentList;
    }

    private BiFunction<HashMap<String, List<Event>>, FirebaseDays, Schedule> combineSessionsById() {
        return (map, apiDays) -> {
            List<SchedulePage> pages = new ArrayList<>(map.size());
            for (String dayId : map.keySet()) {
                Optional<String> date = findDate(apiDays, dayId);
                if (date.isPresent()) {
                    pages.add(SchedulePage.create(date.get(), map.get(dayId)));
                }
            }

            return Schedule.create(pages);
        };
    }

    private Optional<String> findDate(FirebaseDays apiDays, String dayId) {
        return find(apiDays.days, firebaseDay -> firebaseDay.id.equals(String.valueOf(dayId)))
                .map(firebaseDay -> firebaseDay.date);
    }
}
