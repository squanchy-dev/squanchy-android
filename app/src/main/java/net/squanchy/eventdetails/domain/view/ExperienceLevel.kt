package net.squanchy.eventdetails.domain.view

import android.support.annotation.StringRes
import net.squanchy.R
import net.squanchy.support.lang.Optional
import timber.log.Timber
import java.util.Locale

enum class ExperienceLevel(private val rawLevel: String, @StringRes private val labelStringResId: Int) {
    BEGINNER("beginner", R.string.experience_level_beginner),
    INTERMEDIATE("intermediate", R.string.experience_level_intermediate),
    ADVANCED("advanced", R.string.experience_level_advanced);

    @StringRes
    fun labelStringResId(): Int {
        return labelStringResId
    }

    companion object {

        fun fromNullableRawLevel(rawLevel: String): Optional<ExperienceLevel> =
                try {
                    Optional.fromNullable(rawLevel).map({ fromRawLevel(it) })
                } catch (e: IllegalArgumentException) {
                    Timber.d(e)
                    Optional.absent()
                }

        private fun fromRawLevel(rawLevel: String) =
                when (rawLevel.toLowerCase(Locale.US)) {
                    BEGINNER.rawLevel -> BEGINNER
                    INTERMEDIATE.rawLevel -> INTERMEDIATE
                    ADVANCED.rawLevel -> ADVANCED
                    else -> throw IllegalArgumentException("Invalid raw level description: " + rawLevel)
                }
    }
}
