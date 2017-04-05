package net.squanchy.tweets.service;

import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.ArrayList;
import java.util.List;

import net.squanchy.support.lang.Lists;
import net.squanchy.support.lang.Optional;
import net.squanchy.tweets.domain.TweetLinkInfo;
import net.squanchy.tweets.domain.view.TweetViewModel;
import net.squanchy.tweets.domain.view.User;

public class TweetModelConverter {

    private static final String MEDIA_TYPE_PHOTO = "photo";

    private final TweetSpannedTextBuilder tweetSpannedTextBuilder;

    public TweetModelConverter(TweetSpannedTextBuilder tweetSpannedTextBuilder) {
        this.tweetSpannedTextBuilder = tweetSpannedTextBuilder;
    }

    TweetViewModel toViewModel(Tweet tweet) {
        User user = User.create(tweet.user.name, tweet.user.screenName, tweet.user.profileImageUrlHttps);

        List<Integer> emojiIndices = findEmojiIndices(tweet.text);
        Range displayTextRange = Range.from(tweet.displayTextRange, tweet.text.length(), emojiIndices.size());

        List<HashtagEntity> hashtags = adjustHashtag(onlyHashtagsInRange(tweet.entities.hashtags, displayTextRange), emojiIndices);
        List<MentionEntity> mentions = adjustMentions(onlyMentionsInRange(tweet.entities.userMentions, displayTextRange), emojiIndices);
        List<UrlEntity> urls = adjustUrls(onlyUrlsInRange(tweet.entities.urls, displayTextRange), emojiIndices);
        List<String> photoUrls = onlyPhotoUrls(tweet.entities.media);
        String displayableText = displayableTextFor(tweet, displayTextRange);

        return TweetViewModel.builder()
                .id(tweet.id)
                .text(displayableText)
                .spannedText(tweetSpannedTextBuilder.applySpans(displayableText, displayTextRange.start(), hashtags, mentions, urls))
                .createdAt(tweet.createdAt)
                .user(user)
                .photoUrl(photoUrlMaybeFrom(photoUrls))
                .linkInfo(TweetLinkInfo.from(tweet))
                .build();
    }

    private List<Integer> findEmojiIndices(String text) {
        List<Integer> emojiIndices = new ArrayList<>();
        for (int i = 0; i < text.length() - 1; ++i) {
            if (Character.isHighSurrogate(text.charAt(i)) && Character.isLowSurrogate(text.charAt(i + 1))) {
                emojiIndices.add(i);
            }
        }
        return emojiIndices;
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

    private List<HashtagEntity> adjustHashtag(List<HashtagEntity> entities, List<Integer> indices) {
        List<HashtagEntity> hashtags = new ArrayList<>(entities.size());
        for (HashtagEntity hashtag : entities) {
            int offset = offsetFrom(hashtag.getStart(), indices);
            hashtags.add(new HashtagEntity(hashtag.text, hashtag.getStart() + offset, hashtag.getEnd() + offset));
        }
        return hashtags;
    }

    private List<MentionEntity> adjustMentions(List<MentionEntity> entities, List<Integer> indices) {
        List<MentionEntity> mentions = new ArrayList<>(entities.size());
        for (MentionEntity mention : entities) {
            int offset = offsetFrom(mention.getStart(), indices);
            mentions.add(new MentionEntity(mention.id, mention.idStr, mention.name, mention.screenName, mention.getStart() + offset, mention.getEnd() + offset));
        }
        return mentions;
    }

    private List<UrlEntity> adjustUrls(List<UrlEntity> entities, List<Integer> indices) {
        List<UrlEntity> urls = new ArrayList<>(entities.size());
        for (UrlEntity url : entities) {
            int offset = offsetFrom(url.getStart(), indices);
            urls.add(new UrlEntity(url.url, url.expandedUrl, url.displayUrl, url.getStart() + offset, url.getEnd() + offset));
        }
        return urls;
    }

    private int offsetFrom(int start, List<Integer> indices) {
        int offset = 0;
        for (Integer index : indices) {
            if (index - offset <= start) {
                offset += 1;
            } else {
                break;
            }
        }
        return offset;
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

        static Range from(List<Integer> positions, int textLength, int offset) {
            if (positions.size() != 2) {
                return new Range(0, textLength - 1);
            }
            return new Range(positions.get(0), positions.get(1) + offset);
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
