package net.squanchy.tweets.parsing;

import net.squanchy.tweets.model.TweetSpannableText;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SpannableTextExtractorTest {

    private static final String TWEET_TEXT = "@tiwiz look who's speaking at @droidconit this year! https://t.co/wOMBeVINLW #Torino @android #adroiddev #AndDev https://t.co/La0kCuU4aa";

    @Test
    public void givenAnyText_andARange_whenExtractingSpannableText_thenReturnsACorrectSpannableText() {

        TweetSpannableText spannableText = SpannableTextExtractor.extract(TWEET_TEXT, 30, 41);

        TweetSpannableText expectedSpannableText = TweetSpannableText.from("@droidconit", 30, 41);
        assertThat(spannableText).isEqualTo(expectedSpannableText);
    }
}
