package net.squanchy.tweets.util;

import java.util.List;

import net.squanchy.tweets.domain.view.HashtagEntity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashtagParserTest {

    private final HashtagParser parser = new HashtagParser();

    @Test
    public void testAllHashTagsAreRetrievedCorrectly() throws Exception {
        List<HashtagEntity> hashtags = parser.parseDataFrom(TweetTestData.MentionsAndUrls.TEST_TWEET_WITh_MENTIONS_AND_URLS);

        assertEquals(5, hashtags.size());
    }
}
