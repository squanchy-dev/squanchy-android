package net.squanchy.tweets.parsing;

import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.model.TweetSpannableText;

class HashtagParser extends ElementRegexParser<HashtagEntity> {

    @Override
    Regex regex() {
        return Regex.HASHTAG;
    }

    @Override
    HashtagEntity convertFrom(TweetSpannableText data) {
        return HashtagEntity.create(data.text(), data.begin(), data.end());
    }
}
