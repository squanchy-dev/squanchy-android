package net.squanchy.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.support.lang.Checksum;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

class SearchService {

    private final FirebaseDbService dbService;
    private final Checksum checksum;

    SearchService(FirebaseDbService dbService, Checksum checksum) {
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
