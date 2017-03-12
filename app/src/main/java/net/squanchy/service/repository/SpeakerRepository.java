package net.squanchy.service.repository;

import java.util.List;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Checksum;

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
                .map(speakers -> map(speakers, firebaseSpeaker -> Speaker.create(
                        firebaseSpeaker.id,
                        checksum.getChecksumOf(firebaseSpeaker.id),
                        firebaseSpeaker.name,
                        firebaseSpeaker.photo_url
                )));
    }
}
