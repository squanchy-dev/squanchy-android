package net.squanchy.tweets.util;

import java.util.List;

import net.squanchy.tweets.model.TweetSpecialTextData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TweetParserTest {

    private static final String TEST_TWEET = "Look who's speaking at #droidconit this year! #Torino #android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa";

    @Test
    public void testMentionsAreFoundCorrectly() throws Exception {
        List<TweetSpecialTextData> foundItems = TweetParser.parseHashtagsFrom(TEST_TWEET);
        assertEquals(5, foundItems.size());
    }

    @Test
    public void testMentionIsExtractedCorrectly() throws Exception {
        int start = 23;
        int end = 34;
        String text = "#droidconit";

        TweetSpecialTextData data = TweetParser.extract(TEST_TWEET, start, end);

        assertEquals(text, data.text());
    }
}
