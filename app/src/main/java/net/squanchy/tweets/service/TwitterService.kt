package net.squanchy.tweets.service

import io.reactivex.Observable
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.tweets.domain.view.TweetViewModel
import net.squanchy.tweets.view.TweetUrlSpanFactory

internal class TwitterService(
        private val repository: FirestoreDbService,
        private val factory: TweetUrlSpanFactory
) {

    fun refresh(query: String): Observable<List<TweetViewModel>> {
        return repository.twitterView()
            .map { tweets -> tweets.map { mapToViewModel(factory, it) } }
    }
}
