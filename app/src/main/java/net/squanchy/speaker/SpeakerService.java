package net.squanchy.speaker;


import java.util.List;

import net.squanchy.service.firebase.FirebaseSquanchyRepository;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

class SpeakerService {

    private final FirebaseSquanchyRepository repository;

    SpeakerService(FirebaseSquanchyRepository repository) {
        this.repository = repository;
    }

    public Single<List<Speaker>> speakers(){

        return repository.speakers()
                .map(it -> it.speakers)
                .flatMap(Observable::fromIterable)
                .map(Speaker::create)
                .toList()
                .subscribeOn(Schedulers.io());
    }
}
