package net.squanchy.tweets.parsing;

import java.util.List;

import net.squanchy.tweets.domain.view.MentionEntity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MentionParserTest {

    private final MentionParser parser = new MentionParser();

    @Test
    public void allMentionsAreRetrieved() throws Exception {
        List<MentionEntity> mentions = parser.parseDataFrom(TweetTestData.MentionsAndUrls.TEST_TWEET_WITh_MENTIONS_AND_URLS);

        assertEquals(0, mentions.size());
    }
}
