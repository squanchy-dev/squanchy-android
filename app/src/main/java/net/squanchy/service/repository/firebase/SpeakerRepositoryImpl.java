package net.squanchy.service.repository.firebase;

import java.util.List;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Checksum;
import net.squanchy.support.lang.Optional;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

public class SpeakerRepositoryImpl implements SpeakerRepository {

    private final FirebaseDbService dbService;
    private final Checksum checksum;

    public SpeakerRepositoryImpl(FirebaseDbService dbService, Checksum checksum) {
        this.dbService = dbService;
        this.checksum = checksum;
    }


    public Observable<List<Speaker>> speakers() {
        return dbService.speakers()
                .map(firebaseSpeaker -> firebaseSpeaker.getSpeakers())
                .map(speakers -> map(speakers, this::toSpeaker));
    }

    public Observable<Speaker> speaker(String speakerId) {
        return dbService.speakers()
                .map(firebaseSpeakers -> firebaseSpeakers.getSpeakers())
                .flatMap(Observable::fromIterable)
                .filter(firebaseSpeaker -> firebaseSpeaker.getId().equals(speakerId))
                .map(this::toSpeaker);
    }

    private Speaker toSpeaker(FirebaseSpeaker firebaseSpeaker) {
        return Speaker.Companion.create(
                firebaseSpeaker.getId(),
                checksum.getChecksumOf(firebaseSpeaker.getId()),
                firebaseSpeaker.getName(),
                firebaseSpeaker.getBio(),
                Optional.fromNullable(firebaseSpeaker.getCompany_name()),
                Optional.fromNullable(firebaseSpeaker.getCompany_url()),
                Optional.fromNullable(firebaseSpeaker.getPersonal_url()),
                Optional.fromNullable(firebaseSpeaker.getPhoto_url()),
                Optional.fromNullable(firebaseSpeaker.getTwitter_username())
        );
    }
}
