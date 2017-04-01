package net.squanchy.tweets.service;

import java.util.List;

import net.squanchy.tweets.domain.view.TweetViewModel;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.filter;
import static net.squanchy.support.lang.Lists.map;

public class TwitterService {

    private final TwitterRepository repository;
    private final TweetModelConverter modelConverter;

    public TwitterService(TwitterRepository repository, TweetModelConverter modelConverter) {
        this.repository = repository;
        this.modelConverter = modelConverter;
    }

    public Observable<List<TweetViewModel>> refresh(String query) {
        return repository.load(query)
                .map(search -> search.tweets)
                .map(list -> filter(list, tweet -> tweet.retweetedStatus == null))
                .map(tweets -> map(tweets, modelConverter::toViewModel));
    }
}
