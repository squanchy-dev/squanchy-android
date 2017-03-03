package net.squanchy.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.service.firebase.FirebaseDbService;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

class SearchService {

    private final FirebaseDbService dbService;

    SearchService(FirebaseDbService dbService) {
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
