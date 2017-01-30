package com.connfa.schedule.service;

import com.connfa.schedule.domain.view.Event;
import com.connfa.schedule.domain.view.Schedule;
import com.connfa.schedule.domain.view.SchedulePage;
import com.connfa.service.firebase.FirebaseConnfaRepository;
import com.connfa.service.firebase.model.FirebaseEvent;
import com.connfa.service.firebase.model.FirebaseSpeaker;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.connfa.support.lang.Lists.find;
import static com.connfa.support.lang.Lists.map;

public class ScheduleActivityService {

    private final FirebaseConnfaRepository repository;

    public ScheduleActivityService(FirebaseConnfaRepository repository) {
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
                                                    speakerHolder.speakers, speaker -> speaker.speakerId == speakerId)
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
