package net.squanchy.tweets.parsing;

import java.util.List;

import net.squanchy.tweets.domain.view.HashtagEntity;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class HashtagParserTest {

    private static final String TWEET_WITH_HASHTAGS = "Look who's speaking at #droidconit this year! @droidconit #Torino #android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa";
    private static final String TWEET_WITH_NO_HASHTAGS = "Look who's speaking at @droidconit this year! https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa";

    private final HashtagParser parser = new HashtagParser();

    @Test
    public void givenTweetTextWithHashtags_whenParsing_thenReturnsAllHashtags() {

        List<HashtagEntity> hashtags = parser.parseDataFrom(TWEET_WITH_HASHTAGS);

        assertThat(hashtags).containsExactly(
                HashtagEntity.create("#droidconit", 23, 34),
                HashtagEntity.create("#Torino", 46, 53),
                HashtagEntity.create("#android", 54, 62),
                HashtagEntity.create("#adroiddev", 63, 73),
                HashtagEntity.create("#AndDev", 74, 81)

        );
    }
    @Test
    public void givenTweetTextWithNoHashtags_whenParsing_thenReturnsEmptyList() {

        List<HashtagEntity> hashtags = parser.parseDataFrom(TWEET_WITH_NO_HASHTAGS);

        assertThat(hashtags).isEmpty();
    }
}
