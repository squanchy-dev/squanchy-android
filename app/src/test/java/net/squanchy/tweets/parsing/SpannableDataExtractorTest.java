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

        TweetSpannableText data = SpannableDataExtractor.extract(TweetTestData.MentionsAndUrls.TEST_TWEET_WITh_MENTIONS_AND_URLS, start, end);

        assertEquals(text, data.text());
    }
}
