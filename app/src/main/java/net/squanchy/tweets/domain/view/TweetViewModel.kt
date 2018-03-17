package net.squanchy.tweets.domain.view

import net.squanchy.tweets.domain.TweetLinkInfo
import java.util.Date

@Suppress("LongParameterList") // This is just a big model - TODO refactor this to split it up
data class TweetViewModel(
    val id: Long,
    val text: String,
    val spannedText: CharSequence,
    val user: User,
    val createdAt: Date,
    val photoUrl: String?,
    val linkInfo: TweetLinkInfo
)
