package net.squanchy.support.system

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class DebugCurrentTime(context: Context) : CurrentTime {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun currentDateTime(): ZonedDateTime {
        return preferences.getString(KEY_FROZEN_TIME, null)?.let {
            ZonedDateTime.parse(it, DATE_TIME_FORMATTER)
        } ?: ZonedDateTime.now()
    }

    companion object {
        private const val KEY_FROZEN_TIME = "FROZEN_TIME"
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME

        fun freeze(context: Context, frozenDateTime: ZonedDateTime) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit {
                    putString(KEY_FROZEN_TIME, frozenDateTime.format(DATE_TIME_FORMATTER))
                }
        }

        fun unfreeze(context: Context) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit {
                    remove(KEY_FROZEN_TIME)
                }
        }
    }
}
