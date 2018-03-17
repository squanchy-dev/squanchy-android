package net.squanchy.eventdetails.domain.view

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import net.squanchy.R
import net.squanchy.support.lang.Optional
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

        fun tryParsingFrom(rawLevel: String?): Optional<ExperienceLevel> =
            rawLevel?.let {
                try {
                    Optional.of(fromRawLevel(it))
                } catch (e: IllegalArgumentException) {
                    Timber.d(e)
                    Optional.absent<ExperienceLevel>()
                }
            } ?: Optional.absent()

        private fun fromRawLevel(rawLevel: String) =
            when (rawLevel.toLowerCase(Locale.US)) {
                BEGINNER.rawLevel -> BEGINNER
                INTERMEDIATE.rawLevel -> INTERMEDIATE
                ADVANCED.rawLevel -> ADVANCED
                else -> throw IllegalArgumentException("Invalid raw level description: " + rawLevel)
            }
    }
}
