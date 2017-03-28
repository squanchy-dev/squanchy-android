package net.squanchy.tweets.service;

import java.util.List;

import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.domain.view.UrlEntity;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

public class TwitterService {

    private final TwitterRepository repo;

    public TwitterService(TwitterRepository repo) {
        this.repo = repo;
    }

    public Observable<List<Tweet>> refresh(String query) {
        return repo.load(query)
                .map(search -> map(search.tweets, this::toViewModel));
    }

    private Tweet toViewModel(com.twitter.sdk.android.core.models.Tweet tweet) {
        return Tweet.builder()
                .id(tweet.id)
                .text(tweet.text)
                .createdAt(tweet.createdAt)
                .hashtags(parseHashtags(tweet.entities.hashtags))
                .mentions(parseMentions(tweet.entities.userMentions))
                .urls(parseUrls(tweet.entities.urls))
                .build();
    }

    private List<HashtagEntity> parseHashtags(List<com.twitter.sdk.android.core.models.HashtagEntity> entities) {
        return map(entities, entity -> HashtagEntity.create(entity.text, entity.getStart(), entity.getEnd()));
    }

    private List<MentionEntity> parseMentions(List<com.twitter.sdk.android.core.models.MentionEntity> entities) {
        return map(entities, entity -> MentionEntity.create(entity.screenName, entity.getStart(), entity.getEnd()));
    }

    private List<UrlEntity> parseUrls(List<com.twitter.sdk.android.core.models.UrlEntity> entities) {
        return map(entities, entity -> UrlEntity.create(entity.url, entity.getStart(), entity.getEnd()));
    }
}
