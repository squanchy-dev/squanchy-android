package net.squanchy.tweets.util;

import net.squanchy.tweets.model.TweetSpecialTextData;

import org.junit.Test;

import static net.squanchy.tweets.util.TweetTestData.MentionsAndUrls.TEST_TWEET_WITh_MENTIONS_AND_URLS;
import static org.junit.Assert.assertEquals;


public class SpannableDataExtractorTest {

    @Test
    public void testMentionIsExtractedCorrectly() throws Exception {
        int start = 23;
        int end = 34;
        String text = "#droidconit";

        TweetSpecialTextData data = SpannableDataExtractor.extract(TEST_TWEET_WITh_MENTIONS_AND_URLS, start, end);

        assertEquals(text, data.text());
    }
}
