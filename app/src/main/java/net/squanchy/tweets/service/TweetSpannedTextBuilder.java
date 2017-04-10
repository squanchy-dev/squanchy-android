package net.squanchy.tweets.service;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.squanchy.tweets.view.TweetUrlSpan;
import net.squanchy.tweets.view.TweetUrlSpanFactory;

public class TweetSpannedTextBuilder {

    private static final String BASE_TWITTER_URL = "https://twitter.com/";
    private static final String MENTION_URL_TEMPLATE = BASE_TWITTER_URL + "%s";
    private static final String QUERY_URL_TEMPLATE = BASE_TWITTER_URL + "search?q=%s";
    private static final Pattern HTML_ENTITY_PATTERN = Pattern.compile("&#?\\w+;");

    private final TweetUrlSpanFactory spanFactory;

    public TweetSpannedTextBuilder(TweetUrlSpanFactory spanFactory) {
        this.spanFactory = spanFactory;
    }

    Spanned applySpans(String text, int startIndex, List<HashtagEntity> hashtags, List<MentionEntity> mentions, List<UrlEntity> urls) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        for (HashtagEntity hashtag : hashtags) {
            hashtag = offsetStart(hashtag, startIndex);
            builder.setSpan(createUrlSpanFor(hashtag), hashtag.getStart(), hashtag.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (MentionEntity mention : mentions) {
            mention = offsetStart(mention, startIndex);
            builder.setSpan(createUrlSpanFor(mention), mention.getStart(), mention.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (UrlEntity url : urls) {
            url = offsetStart(url, startIndex);
            builder.setSpan(createUrlSpanFor(url), url.getStart(), url.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        unescapeEntities(builder);
        return builder;
    }

    private HashtagEntity offsetStart(HashtagEntity hashtag, int startIndex) {
        return new HashtagEntity(
                hashtag.text,
                hashtag.getStart() - startIndex,
                hashtag.getEnd() - startIndex
        );
    }

    private TweetUrlSpan createUrlSpanFor(HashtagEntity hashtag) {
        return span(String.format(QUERY_URL_TEMPLATE, hashtag.text));
    }

    private TweetUrlSpan span(String url) {
        return spanFactory.createFor(url);
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

    private TweetUrlSpan createUrlSpanFor(MentionEntity mention) {
        return span(String.format(MENTION_URL_TEMPLATE, mention.screenName));
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

    private TweetUrlSpan createUrlSpanFor(UrlEntity url) {
        return span(url.url);
    }

    private void unescapeEntities(SpannableStringBuilder builder) {
        String string = builder.toString();
        Matcher matcher = HTML_ENTITY_PATTERN.matcher(string);

        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            Spanned unescapedEntity = Html.fromHtml(matchResult.group());
            builder.replace(matchResult.start(), matchResult.end(), unescapedEntity);
            unescapeEntities(builder);
        }
    }
}
