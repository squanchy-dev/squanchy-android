package net.squanchy.injection

import android.app.Service
import android.content.Context

import dagger.Module
import dagger.Provides

@Module
class ServiceContextModule(private val service: Service) {

    @Provides
    fun serviceContext(): Context = service
}
