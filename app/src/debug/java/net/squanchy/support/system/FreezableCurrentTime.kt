package net.squanchy.support.system

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class FreezableCurrentTime(context: Context) : CurrentTime {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun currentDateTime(): ZonedDateTime {
        return preferences.getString(KEY_FROZEN_TIME, null)?.let {
            ZonedDateTime.parse(it, DATE_TIME_FORMATTER)
        } ?: ZonedDateTime.now()
    }

    fun freeze(frozenDateTime: ZonedDateTime) {
        preferences.edit { putString(KEY_FROZEN_TIME, frozenDateTime.format(DATE_TIME_FORMATTER)) }
    }

    fun unfreeze() {
        preferences.edit { remove(KEY_FROZEN_TIME) }
    }

    fun isTimeFrozen() = preferences.contains(KEY_FROZEN_TIME)

    companion object {
        private const val KEY_FROZEN_TIME = "FROZEN_TIME"
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME
    }
}
