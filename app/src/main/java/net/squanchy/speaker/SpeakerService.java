package net.squanchy.speaker;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.search.speaker.view.Speaker;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

class SpeakerService {

    private final FirebaseDbService dbService;

    SpeakerService(FirebaseDbService dbService) {
        this.dbService = dbService;
    }

    public Observable<List<Speaker>> speakers() {

        return dbService.speakers()
                .map(firebaseSpeaker -> firebaseSpeaker.speakers)
                .map(list -> map(list, Speaker::create))
                .doOnNext(list -> Collections.sort(list, speakerNameComparator));
    }

    private static final Comparator<Speaker> speakerNameComparator =
            (speaker1, speaker2) -> speaker1.fullName().compareToIgnoreCase(speaker2.fullName());
}
