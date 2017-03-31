package net.squanchy.tweets.parsing;

import net.squanchy.tweets.model.TweetSpecialTextData;

class TweetTestData {

    interface MentionsAndUrls {

        TweetSpecialTextData FIRST_HASHTAG = TweetSpecialTextData.from("#droidconit", 23, 34);
        TweetSpecialTextData SECOND_HASHTAG = TweetSpecialTextData.from("#Torino", 0, 0);
        TweetSpecialTextData THIRD_HASHTAG = TweetSpecialTextData.from("#android", 0, 0);
        TweetSpecialTextData FOURTH_HASHTAG = TweetSpecialTextData.from("#adroiddev", 0, 0);
        TweetSpecialTextData FIFTH_HASHTAG = TweetSpecialTextData.from("#AndDev", 0, 0);

        String TEST_TWEET_WITh_MENTIONS_AND_URLS = "Look who's speaking at #droidconit this year! #Torino #android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa";
    }
}
