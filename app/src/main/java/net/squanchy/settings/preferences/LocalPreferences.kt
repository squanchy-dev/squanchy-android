package net.squanchy.settings.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.Observable

fun showFavoritesInScheduleObservable(context: Context) =
    context.defaultSharedPreferences.observeFlag("favorites_in_schedule_preference_key", false)

private val Context.defaultSharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

private fun SharedPreferences.observeFlag(key: String, defaultValue: Boolean = false): Observable<Boolean> = Observable.create { emitter ->
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
        if (key == changedKey) {
            emitter.onNext(getBoolean(key, defaultValue))
        }
    }

    emitter.onNext(getBoolean(key, defaultValue))
    registerOnSharedPreferenceChangeListener(listener)
    emitter.setCancellable { unregisterOnSharedPreferenceChangeListener(listener) }
}
