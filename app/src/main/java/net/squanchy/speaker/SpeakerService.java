package net.squanchy.speaker;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.lang.Checksum;
import net.squanchy.support.lang.Lists;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

class SpeakerService {

    private final FirebaseDbService dbService;
    private final Checksum checksum;

    SpeakerService(FirebaseDbService dbService, Checksum checksum) {
        this.dbService = dbService;
        this.checksum = checksum;
    }

    public Observable<List<Speaker>> speakers() {

        return dbService.speakers()
                .map(firebaseSpeaker -> firebaseSpeaker.speakers)
                .map(list -> map(list, firebaseSpeaker -> Speaker.create(firebaseSpeaker, checksum.getChecksumOf(firebaseSpeaker.id))))
                .doOnNext(list -> Collections.sort(list, speakerNameComparator));
    }

    private static final Comparator<Speaker> speakerNameComparator =
            (speaker1, speaker2) -> speaker1.name().compareToIgnoreCase(speaker2.name());
}
