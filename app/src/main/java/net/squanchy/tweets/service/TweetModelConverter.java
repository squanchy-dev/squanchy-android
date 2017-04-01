package net.squanchy.tweets.service;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;

import com.google.auto.value.AutoValue;
import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;

import net.squanchy.support.lang.Lists;
import net.squanchy.tweets.domain.view.TweetViewModel;
import net.squanchy.tweets.domain.view.User;

public class TweetModelConverter {

    private static final String MEDIA_TYPE_PHOTO = "photo";
    private static final String BASE_TWITTER_URL = "https://twitter.com/";
    private static final String MENTION_URL_TEMPLATE = BASE_TWITTER_URL + "%s";
    private static final String QUERY_URL_TEMPLATE = BASE_TWITTER_URL + "search?q=%s";

    TweetViewModel toViewModel(Tweet tweet) {
        User user = User.create(tweet.user.name, tweet.user.screenName, tweet.user.profileImageUrlHttps);

        Range displayTextRange = Range.from(tweet.displayTextRange, tweet.text.length());
        List<HashtagEntity> hashtags = onlyHashtagsInRange(tweet.entities.hashtags, displayTextRange);
        List<MentionEntity> mentions = onlyMentionsInRange(tweet.entities.userMentions, displayTextRange);
        List<UrlEntity> urls = onlyUrlsInRange(tweet.entities.urls, displayTextRange);
        List<String> photoUrls = onlyPhotoUrls(tweet.entities.media);
        String displayableText = displayableTextFor(tweet, displayTextRange);

        return TweetViewModel.builder()
                .id(tweet.id)
                .text(displayableText)
                .spannedText(applySpans(displayableText, displayTextRange.start(), hashtags, mentions, urls))
                .createdAt(tweet.createdAt)
                .user(user)
                .photoUrls(photoUrls)
                .build();
    }

    private String displayableTextFor(Tweet tweet, Range displayTextRange) {
        Integer beginIndex = displayTextRange.start();
        Integer endIndex = displayTextRange.end();
        return tweet.text.substring(beginIndex, endIndex);
    }

    private List<HashtagEntity> onlyHashtagsInRange(List<HashtagEntity> entities, Range displayTextRange) {
        return Lists.filter(entities, entity -> displayTextRange.contains(entity.getStart(), entity.getEnd()));
    }

    private List<MentionEntity> onlyMentionsInRange(List<MentionEntity> entities, Range displayTextRange) {
        return Lists.filter(entities, entity -> displayTextRange.contains(entity.getStart(), entity.getEnd()));
    }

    private List<UrlEntity> onlyUrlsInRange(List<UrlEntity> entities, Range displayTextRange) {
        return Lists.filter(entities, entity -> displayTextRange.contains(entity.getStart(), entity.getEnd()));
    }

    private List<String> onlyPhotoUrls(List<MediaEntity> media) {
        List<MediaEntity> photos = Lists.filter(media, mediaEntity -> MEDIA_TYPE_PHOTO.equals(mediaEntity.type));
        return Lists.map(photos, mediaEntity -> mediaEntity.mediaUrlHttps);
    }

    private Spanned applySpans(String text, int startIndex, List<HashtagEntity> hashtags, List<MentionEntity> mentions, List<UrlEntity> urls) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        for (HashtagEntity hashtag : hashtags) {
            hashtag = offsetStart(hashtag, startIndex);
            builder.setSpan(createUrlSpanFor(hashtag), hashtag.getStart(), hashtag.getEnd(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        for (MentionEntity mention : mentions) {
            mention = offsetStart(mention, startIndex);
            builder.setSpan(createUrlSpanFor(mention), mention.getStart(), mention.getEnd(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        for (UrlEntity url : urls) {
            url = offsetStart(url, startIndex);
            builder.setSpan(createUrlSpanFor(url), url.getStart(), url.getEnd(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return builder;
    }

    private HashtagEntity offsetStart(HashtagEntity hashtag, int startIndex) {
        return new HashtagEntity(
                hashtag.text,
                hashtag.getStart() - startIndex,
                hashtag.getEnd() - startIndex
        );
    }

    private URLSpan createUrlSpanFor(HashtagEntity hashtag) {
        return new URLSpan(String.format(QUERY_URL_TEMPLATE, hashtag.text));
    }

    private MentionEntity offsetStart(MentionEntity mention, int startIndex) {
        return new MentionEntity(
                mention.id,
                mention.idStr,
                mention.name,
                mention.screenName,
                mention.getStart() - startIndex,
                mention.getEnd() - startIndex
        );
    }

    private URLSpan createUrlSpanFor(MentionEntity mention) {
        return new URLSpan(String.format(MENTION_URL_TEMPLATE, mention.screenName));
    }

    private UrlEntity offsetStart(UrlEntity url, int startIndex) {
        return new UrlEntity(
                url.url,
                url.expandedUrl,
                url.displayUrl,
                url.getStart() - startIndex,
                url.getEnd() - startIndex
        );
    }

    private URLSpan createUrlSpanFor(UrlEntity url) {
        return new URLSpan(url.url);
    }

    @AutoValue
    abstract static class Range {

        static Range from(List<Integer> positions, int textLength) {
            if (positions.size() != 2) {
                return new AutoValue_TweetModelConverter_Range(0, textLength - 1);
            }
            return new AutoValue_TweetModelConverter_Range(positions.get(0), positions.get(1));
        }

        abstract int start();

        abstract int end();

        boolean contains(int start, int end) {
            return start() <= start && end <= end();
        }
    }
}
