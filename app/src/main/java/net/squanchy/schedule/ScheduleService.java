package net.squanchy.schedule;

import java.util.List;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.service.firebase.FirebaseSquanchyRepository;
import net.squanchy.service.firebase.model.FirebaseDay;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSchedule;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.service.firebase.model.FirebaseSpeakers;
import net.squanchy.support.lang.Lists;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

class ScheduleService {

    private final FirebaseSquanchyRepository repository;

    ScheduleService(FirebaseSquanchyRepository repository) {
        this.repository = repository;
    }

    public Observable<Schedule> schedule() {
        Observable<FirebaseSchedule> eventObservable = repository.sessions();
        Observable<FirebaseSpeakers> speakersObservable = repository.speakers();

        return Observable.combineLatest(
                eventObservable,
                speakersObservable,
                composeIntoSchedule()
        );
    }

    private BiFunction<FirebaseSchedule, FirebaseSpeakers, Schedule> composeIntoSchedule() {
        return (eventHolder, speakerHolder) -> {
            List<SchedulePage> pages = map(eventHolder.days, toSchedulePage(eventHolder, speakerHolder));
            return Schedule.create(pages);
        };
    }

    private Lists.Function<FirebaseDay, SchedulePage> toSchedulePage(FirebaseSchedule eventHolder, FirebaseSpeakers speakerHolder) {
        return day -> SchedulePage.create(
                day.date,
                map(day.events, toEvent(eventHolder, speakerHolder, day))
        );
    }

    private Lists.Function<FirebaseEvent, Event> toEvent(FirebaseSchedule eventHolder, FirebaseSpeakers speakerHolder, FirebaseDay day) {
        return firebaseEvent -> {
            List<FirebaseSpeaker> speakers = speakersForEvent(firebaseEvent, speakerHolder);
            return Event.create(
                    firebaseEvent.eventId,
                    eventHolder.days.indexOf(day),      // TODO do this less crappily
                    firebaseEvent.name,
                    firebaseEvent.place,
                    firebaseEvent.experienceLevel,
                    map(speakers, toSpeakerName()));
        };
    }

    private List<FirebaseSpeaker> speakersForEvent(FirebaseEvent firebaseEvent, FirebaseSpeakers speakerHolder) {
        return map(firebaseEvent.speakers, speakerId -> findSpeaker(speakerHolder, speakerId));
    }

    private FirebaseSpeaker findSpeaker(FirebaseSpeakers speakerHolder, Long speakerId) {
        return find(speakerHolder.speakers, speaker -> speaker.speakerId.equals(speakerId));
    }

    private Lists.Function<FirebaseSpeaker, String> toSpeakerName() {
        return firebaseSpeaker -> firebaseSpeaker != null ? firebaseSpeaker.firstName + " " + firebaseSpeaker.lastName : null;
    }
}
