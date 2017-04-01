package net.squanchy.tweets.parsing;

import android.support.v4.util.PatternsCompat;

import java.util.regex.Pattern;

import net.squanchy.tweets.domain.view.UrlEntity;
import net.squanchy.tweets.model.TweetSpannableText;

class UrlParser extends TweetParser<UrlEntity> {

    @Override
    Pattern pattern() {
        return PatternsCompat.WEB_URL;
    }

    @Override
    UrlEntity convertFrom(TweetSpannableText data) {
        return UrlEntity.create(data.text(), data.begin(), data.end());
    }
}
