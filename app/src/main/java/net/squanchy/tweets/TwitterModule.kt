package net.squanchy.tweets

import android.app.Activity
import dagger.Module
import dagger.Provides
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.tweets.service.TwitterService
import net.squanchy.tweets.view.TweetUrlSpanFactory

@Module
internal class TwitterModule {

    @Provides
    fun provideUrlSpanFactory(activity: Activity): TweetUrlSpanFactory =
        TweetUrlSpanFactory(activity)

    @Provides
    fun twitterService(repository: FirestoreDbService, factory: TweetUrlSpanFactory) =
        TwitterService(repository, factory)
}
