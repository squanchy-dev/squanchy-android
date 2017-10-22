package net.squanchy.tweets

import android.app.Activity

import net.squanchy.tweets.service.TweetModelConverter
import net.squanchy.tweets.service.TweetSpannedTextBuilder
import net.squanchy.tweets.service.TwitterRepository
import net.squanchy.tweets.service.TwitterService
import net.squanchy.tweets.view.TweetUrlSpanFactory

import dagger.Module
import dagger.Provides

@Module
internal class TwitterModule {

    @Provides
    fun twitterRepository() = TwitterRepository()

    @Provides
    fun tweetSpannedTextBuilder(activity: Activity): TweetSpannedTextBuilder {
        val spanFactory = TweetUrlSpanFactory(activity)
        return TweetSpannedTextBuilder(spanFactory)
    }

    @Provides
    fun tweetModelConverter(tweetSpannedTextBuilder: TweetSpannedTextBuilder) =
        TweetModelConverter(tweetSpannedTextBuilder)

    @Provides
    fun twitterService(repository: TwitterRepository, modelConverter: TweetModelConverter) =
        TwitterService(repository, modelConverter)
}
