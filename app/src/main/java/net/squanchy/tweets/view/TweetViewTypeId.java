package net.squanchy.tweets.view;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TweetViewTypeId.TWEET, TweetViewTypeId.LOADING})
@Retention(RetentionPolicy.SOURCE)
@interface TweetViewTypeId {

    int TWEET = 0;
    int LOADING = 1;
}
