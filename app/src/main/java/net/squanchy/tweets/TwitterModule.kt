package net.squanchy.tweets

import android.app.Activity
import dagger.Module
import dagger.Provides
import net.squanchy.tweets.service.TwitterRepository
import net.squanchy.tweets.service.TwitterService
import net.squanchy.tweets.view.TweetUrlSpanFactory

@Module
internal class TwitterModule {

    @Provides
    fun twitterRepository() = TwitterRepository()

    @Provides
    fun provideUrlSpanFactory(activity: Activity): TweetUrlSpanFactory =
        TweetUrlSpanFactory(activity)

    @Provides
    fun twitterService(repository: TwitterRepository, factory: TweetUrlSpanFactory) =
        TwitterService(repository, factory)
}
