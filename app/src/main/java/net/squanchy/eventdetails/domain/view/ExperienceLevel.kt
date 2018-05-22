package net.squanchy.eventdetails.domain.view

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import net.squanchy.R
import net.squanchy.schedule.domain.view.ExperienceLevel

val ExperienceLevel.labelStringResId
    @StringRes get() = when (this) {
        ExperienceLevel.BEGINNER -> R.string.experience_level_beginner
        ExperienceLevel.INTERMEDIATE -> R.string.experience_level_intermediate
        ExperienceLevel.ADVANCED -> R.string.experience_level_advanced
    }

val ExperienceLevel.colorResId
    @ColorRes get() = when (this) {
        ExperienceLevel.BEGINNER -> R.color.experience_level_beginner
        ExperienceLevel.INTERMEDIATE -> R.color.experience_level_intermediate
        ExperienceLevel.ADVANCED -> R.color.experience_level_advanced
    }
