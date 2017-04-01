package net.squanchy.tweets.parsing;

import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.model.TweetSpannableText;

class MentionParser extends ElementRegexParser<MentionEntity> {

    @Override
    Regex regex() {
        return Regex.MENTION;
    }

    @Override
    MentionEntity convertFrom(TweetSpannableText data) {
        return MentionEntity.create(data.text(), data.begin(), data.end());
    }
}
