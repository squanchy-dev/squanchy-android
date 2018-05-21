package net.squanchy.speaker.domain.view

import arrow.core.Option

@Suppress("LongParameterList") // This is just a big model - TODO refactor this to split it up
data class Speaker(
    val numericId: Long,
    val id: String,
    val name: String,
    val bio: String,
    val companyName: Option<String>,
    val companyUrl: Option<String>,
    val personalUrl: Option<String>,
    val photoUrl: Option<String>,
    val twitterUsername: Option<String>
)
