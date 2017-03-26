package net.squanchy.service.repository;

import java.util.List;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Checksum;
import net.squanchy.support.lang.Optional;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

public class SpeakerRepository {

    private final FirebaseDbService dbService;
    private final Checksum checksum;

    public SpeakerRepository(FirebaseDbService dbService, Checksum checksum) {
        this.dbService = dbService;
        this.checksum = checksum;
    }

    public Observable<List<Speaker>> speakers() {
        return dbService.speakers()
                .map(firebaseSpeaker -> firebaseSpeaker.speakers)
                .map(speakers -> map(speakers, this::toSpeaker));
    }

    public Observable<Speaker> speaker(String speakerId) {
        return dbService.speakers()
                .map(firebaseSpeakers -> firebaseSpeakers.speakers)
                .flatMap(Observable::fromIterable)
                .filter(firebaseSpeaker -> firebaseSpeaker.id.equals(speakerId))
                .map(this::toSpeaker);
    }

    private Speaker toSpeaker(FirebaseSpeaker firebaseSpeaker) {
        return Speaker.create(
                firebaseSpeaker.id,
                checksum.getChecksumOf(firebaseSpeaker.id),
                firebaseSpeaker.name,
                firebaseSpeaker.bio,
                Optional.fromNullable(firebaseSpeaker.company_name),
                Optional.fromNullable(firebaseSpeaker.company_url),
                Optional.fromNullable(firebaseSpeaker.personal_url),
                Optional.fromNullable(firebaseSpeaker.photo_url),
                Optional.fromNullable(firebaseSpeaker.twitter_username)
        );
    }
}
