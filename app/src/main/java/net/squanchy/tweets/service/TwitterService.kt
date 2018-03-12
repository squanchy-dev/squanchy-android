package net.squanchy.tweets.service

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.tweets.domain.view.TwitterScreenViewModel
import net.squanchy.tweets.view.TweetUrlSpanFactory

internal class TwitterService(
    dbService: FirestoreDbService,
    private val factory: TweetUrlSpanFactory
) {

    private val tweets = dbService.twitterView()
        .map { tweets -> tweets.sortedByDescending { it.createdAt } }
        .map { tweets -> tweets.map { mapToViewModel(factory, it) } }

    private val hashtag = dbService.conferenceInfo()
        .map { it.socialHashtag }

    fun refresh(): Observable<TwitterScreenViewModel> {
        return Observable.combineLatest(tweets, hashtag, BiFunction { tweets, hashtag -> TwitterScreenViewModel(hashtag, tweets) })
    }
}
