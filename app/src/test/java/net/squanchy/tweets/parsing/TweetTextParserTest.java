package net.squanchy.tweets.parsing;

import java.util.Arrays;
import java.util.List;

import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.domain.view.UrlEntity;
import net.squanchy.tweets.model.ParsedTweet;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TweetTextParserTest {

    private static final String TWEET_TEXT = "Look who's speaking at #droidconit this year! @droidconit #Torino #android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa @android";


    private final TweetTextParser parser = new TweetTextParser();

    @Test
    public void givenATweet_whenParsing_thenReturnsParsedTweetWithAllElements() {

        ParsedTweet tweetData = parser.parse(TWEET_TEXT);

        assertThat(tweetData).isEqualTo(expectedTweetData());
    }

    private ParsedTweet expectedTweetData() {
        return ParsedTweet.create(
                expectedHashtags(),
                expectedMentions(),
                expectedUrls()
        );
    }

    private List<HashtagEntity> expectedHashtags() {
        return Arrays.asList(
                HashtagEntity.create("#droidconit", 23, 34),
                HashtagEntity.create("#Torino", 58, 65),
                HashtagEntity.create("#android", 66, 74),
                HashtagEntity.create("#adroiddev", 75, 85),
                HashtagEntity.create("#AndDev", 86, 93)
        );
    }

    private List<MentionEntity> expectedMentions() {
        return Arrays.asList(
                MentionEntity.create("@droidconit", 46, 57),
                MentionEntity.create("@android", 142, 150)
        );
    }

    private List<UrlEntity> expectedUrls() {
        return Arrays.asList(
                UrlEntity.create("https://t.co/wOMBeVINLW", 94, 117),
                UrlEntity.create("https://t.co/La0kCuU4aa", 118, 141)
        );
    }
}
