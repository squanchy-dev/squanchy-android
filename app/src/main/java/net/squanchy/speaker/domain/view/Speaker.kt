package net.squanchy.speaker.domain.view

import arrow.core.Option

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
