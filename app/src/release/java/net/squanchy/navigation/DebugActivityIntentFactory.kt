package net.squanchy.navigation

import android.content.Context
import android.content.Intent

class DebugActivityIntentFactory {

    fun createDebugActivityIntent(context: Context): Intent {
        throw UnsupportedOperationException("The Debug activity is not available on release builds")
    }
}
