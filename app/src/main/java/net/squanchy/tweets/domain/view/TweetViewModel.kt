package net.squanchy.tweets.domain.view

import com.google.firebase.Timestamp
import net.squanchy.tweets.domain.TweetLinkInfo

data class TweetViewModel(
    val id: Long,
    val text: String,
    val spannedText: CharSequence,
    val user: User,
    val createdAt: Timestamp,
    val photoUrl: String?,
    val linkInfo: TweetLinkInfo
)
