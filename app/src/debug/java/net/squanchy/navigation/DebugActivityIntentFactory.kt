package net.squanchy.navigation

import android.content.Context
import android.content.Intent

import net.squanchy.support.debug.DebugActivity

class DebugActivityIntentFactory {

    fun createDebugActivityIntent(context: Context) = Intent(context, DebugActivity::class.java)
}
