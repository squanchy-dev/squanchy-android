package net.squanchy.tweets.parsing;

import net.squanchy.tweets.model.TweetSpannableText;

class TweetTestData {

    interface MentionsAndUrls {

        TweetSpannableText FIRST_HASHTAG = TweetSpannableText.from("#droidconit", 23, 34);
        TweetSpannableText SECOND_HASHTAG = TweetSpannableText.from("#Torino", 0, 0);
        TweetSpannableText THIRD_HASHTAG = TweetSpannableText.from("#android", 0, 0);
        TweetSpannableText FOURTH_HASHTAG = TweetSpannableText.from("#adroiddev", 0, 0);
        TweetSpannableText FIFTH_HASHTAG = TweetSpannableText.from("#AndDev", 0, 0);

        String TEST_TWEET_WITh_MENTIONS_AND_URLS = "Look who's speaking at #droidconit this year! #Torino #android #adroiddev #AndDev https://t.co/wOMBeVINLW https://t.co/La0kCuU4aa";
    }
}
