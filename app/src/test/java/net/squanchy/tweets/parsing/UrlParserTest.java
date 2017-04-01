package net.squanchy.tweets.parsing;

import java.util.List;

import net.squanchy.tweets.domain.view.UrlEntity;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class UrlParserTest {

    private static final String TWEET_WITH_URLS = "@tiwiz look who's speaking at @droidconit this year! https://t.co/wOMBeVINLW #Torino @android #adroiddev #AndDev https://t.co/La0kCuU4aa";
    private static final String TWEET_WITH_NO_URLS = "@tiwiz look who's speaking at @droidconit this year! #Torino @android #adroiddev #AndDev";

    private final UrlParser parser = new UrlParser();

    @Test
    public void givenTweetTextWithHashtags_whenParsing_thenReturnsAllHashtags() {

        List<UrlEntity> urls = parser.parseDataFrom(TWEET_WITH_URLS);

        assertThat(urls).containsExactly(
                UrlEntity.create("https://t.co/wOMBeVINLW", 53, 76),
                UrlEntity.create("https://t.co/La0kCuU4aa", 113, 136)
        );
    }

    @Test
    public void givenTweetTextWithNoHashtags_whenParsing_thenReturnsEmptyList() {

        List<UrlEntity> urls = parser.parseDataFrom(TWEET_WITH_NO_URLS);

        assertThat(urls).isEmpty();
    }
}
