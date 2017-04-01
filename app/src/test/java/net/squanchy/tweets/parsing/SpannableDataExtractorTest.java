package net.squanchy.tweets.parsing;

import net.squanchy.tweets.model.TweetSpannableText;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class SpannableDataExtractorTest {

    @Test
    public void testMentionIsExtractedCorrectly() throws Exception {
        int start = 23;
        int end = 34;
        String text = "#droidconit";

        TweetSpannableText data = SpannableDataExtractor.extract("Look who's speaking at #droidconit this year! #Torino #android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa", start, end);

        assertEquals(text, data.text());
    }
}
