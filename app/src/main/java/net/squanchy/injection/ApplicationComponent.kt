package net.squanchy.injection

import android.app.Application
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.AnalyticsModule
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.remoteconfig.RemoteConfigModule
import net.squanchy.service.firebase.FirebaseAuthService
import net.squanchy.service.firebase.FirebaseDbService
import net.squanchy.service.firebase.injection.FirebaseModule
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.service.repository.VenueRepository
import net.squanchy.service.repository.injection.RepositoryModule
import net.squanchy.support.injection.ChecksumModule
import net.squanchy.support.injection.CurrentTimeModule

fun createApplicationComponent(application: Application): ApplicationComponent {
    return DaggerApplicationComponent.builder()
        .firebaseModule(FirebaseModule())
        .repositoryModule(RepositoryModule())
        .checksumModule(ChecksumModule())
        .applicationContextModule(ApplicationContextModule(application))
        .analyticsModule(AnalyticsModule(application))
        .remoteConfigModule(RemoteConfigModule())
        .build()
}

@ApplicationLifecycle
@Component(
        modules = [
            ApplicationContextModule::class,
            FirebaseModule::class,
            ChecksumModule::class,
            RepositoryModule::class,
            AnalyticsModule::class,
            RemoteConfigModule::class,
            CurrentTimeModule::class
        ]
)
interface ApplicationComponent {

    fun firebaseDbService(): FirebaseDbService

    fun firebaseAuthService(): FirebaseAuthService

    fun eventRepository(): EventRepository

    fun speakerRepository(): SpeakerRepository

    fun venueRepository(): VenueRepository

    fun analytics(): Analytics

    fun remoteConfig(): RemoteConfig

    fun application(): Application
}
