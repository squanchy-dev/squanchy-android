package net.squanchy.tweets.parsing;

import java.util.List;

import net.squanchy.tweets.domain.view.MentionEntity;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class MentionParserTest {

    private static final String TWEET_WITH_MENTIONS = "@tiwiz look who's speaking at @droidconit this year! #Torino @android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa";
    private static final String TWEET_WITH_NO_MENTIONS = "Look who's speaking at #droidconit this year! #Torino #android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa";

    private final MentionParser parser = new MentionParser();

    @Test
    public void givenTweetTextWithMentions_whenParsing_thenReturnsAllMentions() {

        List<MentionEntity> mentions = parser.parseDataFrom(TWEET_WITH_MENTIONS);

        assertThat(mentions).containsExactly(
                MentionEntity.create("@tiwiz", 0, 6),
                MentionEntity.create("@droidconit", 30, 41),
                MentionEntity.create("@android", 61, 69)
        );
    }

    @Test
    public void givenTweetTextWithNoMentions_whenParsing_thenReturnsEmptyList() {

        List<MentionEntity> mentions = parser.parseDataFrom(TWEET_WITH_NO_MENTIONS);

        assertThat(mentions).isEmpty();
    }
}
