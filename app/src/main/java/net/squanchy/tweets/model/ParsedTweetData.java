package net.squanchy.tweets.model;

import com.google.auto.value.AutoValue;

import java.util.List;

import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.domain.view.UrlEntity;

@AutoValue
public abstract class ParsedTweetData {

    public abstract List<HashtagEntity> hashtags();

    public abstract List<MentionEntity> mentions();

    public abstract List<UrlEntity> urls();

    public static ParsedTweetData create(List<HashtagEntity> hashtags,
                                         List<MentionEntity> mentions,
                                         List<UrlEntity> urls) {

        return new AutoValue_ParsedTweetData(hashtags, mentions, urls);
    }
}
