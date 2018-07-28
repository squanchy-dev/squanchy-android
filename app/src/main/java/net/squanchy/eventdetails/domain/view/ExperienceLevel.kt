package net.squanchy.eventdetails.domain.view

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import arrow.core.Option
import net.squanchy.R
import timber.log.Timber
import java.util.Locale

enum class ExperienceLevel(
    private val rawLevel: String,
    @StringRes val labelStringResId: Int,
    @ColorRes val colorResId: Int
) {
    BEGINNER("beginner", R.string.experience_level_beginner, R.color.experience_level_beginner),
    INTERMEDIATE("intermediate", R.string.experience_level_intermediate, R.color.experience_level_intermediate),
    ADVANCED("advanced", R.string.experience_level_advanced, R.color.experience_level_advanced);

    companion object {

        fun tryParsingFrom(rawLevel: String?): Option<ExperienceLevel> =
            rawLevel?.let {
                try {
                    Option(fromRawLevel(it))
                } catch (e: IllegalArgumentException) {
                    Timber.d(e)
                    null
                }
            } ?: Option.empty()

        private fun fromRawLevel(rawLevel: String) =
            when (rawLevel.toLowerCase(Locale.US)) {
                BEGINNER.rawLevel -> BEGINNER
                INTERMEDIATE.rawLevel -> INTERMEDIATE
                ADVANCED.rawLevel -> ADVANCED
                else -> throw IllegalArgumentException("Invalid raw level description: $rawLevel")
            }
    }
}
