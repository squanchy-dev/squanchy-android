package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class TwitterService {

    private final TwitterRepository repo;

    public TwitterService(TwitterRepository repo) {
        this.repo = repo;
    }

    public Observable<Search> refresh() {
        return repo.load();
    }
}
