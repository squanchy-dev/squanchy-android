package net.squanchy.tweets.parsing;

import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.model.TweetSpecialTextData;

class MentionParser extends TweetRegexParser<MentionEntity> {

    @Override
    Regex regex() {
        return Regex.MENTION;
    }

    @Override
    MentionEntity convertFrom(TweetSpecialTextData data) {
        return MentionEntity.create(data.text(), data.begin(), data.end());
    }
}
