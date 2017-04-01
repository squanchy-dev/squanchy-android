package net.squanchy.tweets.parsing;

import java.util.regex.Pattern;

enum Regex {
    MENTION(Pattern.compile("@\\w+")),
    HASHTAG(Pattern.compile("#\\w+"));

    private final Pattern pattern;

    Regex(Pattern pattern) {
        this.pattern = pattern;
    }

    Pattern pattern() {
        return pattern;
    }
}
