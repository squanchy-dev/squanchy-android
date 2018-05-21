package com.squanchy.schedule.domain.view

data class Track(
    val id: String,
    val numericId: Long,
    val name: String,
    val accentColor: String? = null,
    val textColor: String? = null,
    val iconUrl: String? = null
)
