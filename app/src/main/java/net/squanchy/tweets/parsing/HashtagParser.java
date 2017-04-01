package net.squanchy.tweets.parsing;

import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.model.TweetSpecialTextData;

class HashtagParser extends TweetRegexParserTemplate<HashtagEntity> {

    @Override
    String regex() {
        return ParsingRegex.HASHTAG_REGEX;
    }

    @Override
    HashtagEntity convertFrom(TweetSpecialTextData data) {
        return HashtagEntity.create(data.text(), data.begin(), data.end());
    }
}
