package net.squanchy.speaker;

import java.util.List;

import net.squanchy.service.firebase.FirebaseSquanchyRepository;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

class SpeakerService {

    private final FirebaseSquanchyRepository repository;

    SpeakerService(FirebaseSquanchyRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Speaker>> speakers() {

        return repository.speakers()
                .map(firebaseSpeaker -> firebaseSpeaker.speakers)
                .map(list -> map(list, Speaker::create));
    }
}
