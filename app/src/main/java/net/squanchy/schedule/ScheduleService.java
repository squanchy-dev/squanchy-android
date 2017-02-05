package net.squanchy.schedule;

import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.service.firebase.FirebaseConnfaRepository;
import net.squanchy.service.firebase.model.FirebaseEvent;
import net.squanchy.service.firebase.model.FirebaseSpeaker;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static net.squanchy.support.lang.Lists.find;
import static net.squanchy.support.lang.Lists.map;

class ScheduleService {

    private final FirebaseConnfaRepository repository;

    ScheduleService(FirebaseConnfaRepository repository) {
        this.repository = repository;
    }

    public Observable<Schedule> schedule() {
        Observable<FirebaseEvent.Holder> eventObservable = repository.sessions();
        Observable<FirebaseSpeaker.Holder> speakersObservable = repository.speakers();

        return Observable.combineLatest(
                eventObservable,
                speakersObservable,
                (eventHolder, speakerHolder) -> {

                    List<SchedulePage> pages = map(eventHolder.days, day -> SchedulePage.create(
                            day.date,
                            map(day.events, firebaseEvent -> Event.create(
                                    firebaseEvent.eventId,
                                    firebaseEvent.name,
                                    firebaseEvent.place,
                                    firebaseEvent.experienceLevel,
                                    map(
                                            map(firebaseEvent.speakers, speakerId -> find(
                                                    speakerHolder.speakers, speaker -> speaker.speakerId.equals(speakerId))
                                            ),
                                            speaker -> speaker == null ? "" : speaker.firstName + " " + speaker.lastName
                                    ))
                            )
                    ));

                    return Schedule.create(pages);
                }
        ).subscribeOn(Schedulers.io());
    }
}
