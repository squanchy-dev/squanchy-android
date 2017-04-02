package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;

import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.Optional;
import net.squanchy.tweets.domain.TweetLinkInfo;
import net.squanchy.tweets.domain.view.TweetViewModel;
import net.squanchy.tweets.domain.view.User;

import static android.R.attr.id;

public class TweetModelConverter {

    private static final String MEDIA_TYPE_PHOTO = "photo";

    private final TweetSpannedTextBuilder tweetSpannedTextBuilder;

    public TweetModelConverter(TweetSpannedTextBuilder tweetSpannedTextBuilder) {
        this.tweetSpannedTextBuilder = tweetSpannedTextBuilder;
    }

    TweetViewModel toViewModel(Tweet tweet) {
        User user = User.Companion.create(tweet.user.name, tweet.user.screenName, tweet.user.profileImageUrlHttps);

        Range displayTextRange = Range.from(tweet.displayTextRange, tweet.text.length());
        List<HashtagEntity> hashtags = onlyHashtagsInRange(tweet.entities.hashtags, displayTextRange);
        List<MentionEntity> mentions = onlyMentionsInRange(tweet.entities.userMentions, displayTextRange);
        List<UrlEntity> urls = onlyUrlsInRange(tweet.entities.urls, displayTextRange);
        List<String> photoUrls = onlyPhotoUrls(tweet.entities.media);
        String displayableText = displayableTextFor(tweet, displayTextRange);

        return TweetViewModel.Companion.create(
                id,
                displayableText,
                tweetSpannedTextBuilder.applySpans(displayableText, displayTextRange.start(), hashtags, mentions, urls),
                user,
                tweet.createdAt,
                photoUrlMaybeFrom(photoUrls),
                TweetLinkInfo.Companion.create(tweet)
        );
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

    private Optional<String> photoUrlMaybeFrom(List<String> urls) {
        if (urls.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(urls.get(0));
    }

    private static class Range {

        private final int start;
        private final int end;

        static Range from(List<Integer> positions, int textLength) {
            if (positions.size() != 2) {
                return new Range(0, textLength - 1);
            }
            return new Range(positions.get(0), positions.get(1));
        }

        private Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        int start() {
            return start;
        }

        int end() {
            return end;
        }

        boolean contains(int start, int end) {
            return start() <= start && end <= end();
        }
    }
}
