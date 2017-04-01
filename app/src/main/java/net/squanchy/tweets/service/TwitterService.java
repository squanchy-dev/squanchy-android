package net.squanchy.tweets.service;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;

import com.google.auto.value.AutoValue;
import com.twitter.sdk.android.core.models.MediaEntity;

import java.util.List;

import net.squanchy.support.lang.Lists;
import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.domain.view.UrlEntity;
import net.squanchy.tweets.domain.view.User;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.filter;
import static net.squanchy.support.lang.Lists.map;

public class TwitterService {

    private static final String MEDIA_TYPE_PHOTO = "photo";
    private static final String BASE_TWITTER_URL = "https://twitter.com/";
    private static final String MENTION_URL_TEMPLATE = BASE_TWITTER_URL + "%s";
    private static final String QUERY_URL_TEMPLATE = BASE_TWITTER_URL + "search?q=%s";

    private final TwitterRepository repository;

    public TwitterService(TwitterRepository repository) {
        this.repository = repository;
    }

    public Observable<List<Tweet>> refresh(String query) {
        return repository.load(query)
                .map(search -> search.tweets)
                .map(list -> filter(list, tweet -> tweet.retweetedStatus == null))
                .map(tweets -> map(tweets, this::toViewModel));
    }

    private Tweet toViewModel(com.twitter.sdk.android.core.models.Tweet tweet) {
        User user = User.create(tweet.user.name, tweet.user.screenName, tweet.user.profileImageUrlHttps);

        Range displayTextRange = Range.from(tweet.displayTextRange, tweet.text.length());
        List<HashtagEntity> hashtags = parseHashtags(tweet.entities.hashtags, displayTextRange);
        List<MentionEntity> mentions = parseMentions(tweet.entities.userMentions, displayTextRange);
        List<UrlEntity> urls = parseUrls(tweet.entities.urls, displayTextRange);
        List<String> media = parseMedia(tweet.entities.media, displayTextRange);
        String displayableText = displayableTextFor(tweet, displayTextRange);

        return Tweet.builder()
                .id(tweet.id)
                .text(displayableText)
                .spannedText(applySpans(displayableText, displayTextRange.start(), hashtags, mentions, urls))
                .createdAt(tweet.createdAt)
                .user(user)
                .mediaUrls(media)
                .build();
    }

    private String displayableTextFor(com.twitter.sdk.android.core.models.Tweet tweet, Range displayTextRange) {
        Integer beginIndex = displayTextRange.start();
        Integer endIndex = displayTextRange.end();
        return tweet.text.substring(beginIndex, endIndex);
    }

    private List<HashtagEntity> parseHashtags(List<com.twitter.sdk.android.core.models.HashtagEntity> entities, Range displayTextRange) {
        List<com.twitter.sdk.android.core.models.HashtagEntity> visibleEntities = Lists.filter(entities, entity ->
                displayTextRange.contains(entity.getStart(), entity.getEnd())
        );
        return map(visibleEntities, entity -> HashtagEntity.create(entity.text, entity.getStart(), entity.getEnd()));
    }

    private List<MentionEntity> parseMentions(List<com.twitter.sdk.android.core.models.MentionEntity> entities, Range displayTextRange) {
        List<com.twitter.sdk.android.core.models.MentionEntity> visibleEntities = Lists.filter(entities, entity ->
                displayTextRange.contains(entity.getStart(), entity.getEnd())
        );
        return map(visibleEntities, entity -> MentionEntity.create(entity.screenName, entity.getStart(), entity.getEnd()));
    }

    private List<UrlEntity> parseUrls(List<com.twitter.sdk.android.core.models.UrlEntity> entities, Range displayTextRange) {
        List<com.twitter.sdk.android.core.models.UrlEntity> visibleEntities = Lists.filter(entities, entity ->
                displayTextRange.contains(entity.getStart(), entity.getEnd())
        );
        return map(visibleEntities, entity -> UrlEntity.create(entity.url, entity.getStart(), entity.getEnd()));
    }

    private List<String> parseMedia(List<MediaEntity> media, Range displayTextRange) {
        List<MediaEntity> photos = Lists.filter(media, mediaEntity -> MEDIA_TYPE_PHOTO.equals(mediaEntity.type));
        List<com.twitter.sdk.android.core.models.MediaEntity> visibleEntities = Lists.filter(photos, entity ->
                displayTextRange.contains(entity.getStart(), entity.getEnd())
        );
        return map(visibleEntities, mediaEntity -> mediaEntity.mediaUrlHttps);
    }

    private Spanned applySpans(String text, int startIndex, List<HashtagEntity> hashtags, List<MentionEntity> mentions, List<UrlEntity> urls) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        for (HashtagEntity hashtag : hashtags) {
            hashtag = hashtag.offsetForStart(startIndex);
            builder.setSpan(createUrlSpanFor(hashtag), hashtag.start(), hashtag.end(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        for (MentionEntity mention : mentions) {
            mention = mention.offsetForStart(startIndex);
            builder.setSpan(createUrlSpanFor(mention), mention.start(), mention.end(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        for (UrlEntity url : urls) {
            url = url.offsetForStart(startIndex);
            builder.setSpan(createUrlSpanFor(url), url.start(), url.end(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return builder;
    }

    private URLSpan createUrlSpanFor(HashtagEntity hashtag) {
        return new URLSpan(String.format(QUERY_URL_TEMPLATE, hashtag.text()));
    }

    private URLSpan createUrlSpanFor(MentionEntity mention) {
        return new URLSpan(String.format(MENTION_URL_TEMPLATE, mention.displayName()));
    }

    private URLSpan createUrlSpanFor(UrlEntity url) {
        return new URLSpan(url.url());
    }

    @AutoValue
    abstract static class Range {

        static Range from(List<Integer> positions, int textLength) {
            if (positions.size() != 2) {
                return new AutoValue_TwitterService_Range(0, textLength - 1);
            }
            return new AutoValue_TwitterService_Range(positions.get(0), positions.get(1));
        }

        abstract int start();

        abstract int end();

        boolean contains(int start, int end) {
            return start() <= start && end <= end();
        }
    }
}
