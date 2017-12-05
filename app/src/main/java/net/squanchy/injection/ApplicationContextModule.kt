package net.squanchy.injection

import android.app.Application

import dagger.Module
import dagger.Provides

@Module
class ApplicationContextModule internal constructor(private val application: Application) {

    @Provides
    internal fun applicationContext(): Application = application
}
