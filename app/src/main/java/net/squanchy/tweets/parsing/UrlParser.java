package net.squanchy.tweets.parsing;

import android.util.Patterns;

import java.util.regex.Pattern;

import net.squanchy.tweets.domain.view.UrlEntity;
import net.squanchy.tweets.model.TweetSpecialTextData;

public class UrlParser extends TweetParserTemplate<UrlEntity>{

    @Override
    Pattern pattern() {
        return Patterns.WEB_URL;
    }

    @Override
    UrlEntity convertFrom(TweetSpecialTextData data) {
        return UrlEntity.create(data.text(), data.begin(), data.end());
    }
}
