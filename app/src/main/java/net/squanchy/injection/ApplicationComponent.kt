package net.squanchy.injection

import android.app.Application
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.AnalyticsModule
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.remoteconfig.RemoteConfigModule
import net.squanchy.schedule.tracksfilter.TracksFilter
import net.squanchy.schedule.tracksfilter.TracksFilterModule
import net.squanchy.service.firestore.FirebaseAuthService
import net.squanchy.service.firestore.FirestoreDbService
import net.squanchy.service.firestore.injection.FirestoreModule
import net.squanchy.service.repository.firestore.EventRepository
import net.squanchy.service.repository.firestore.SpeakerRepository
import net.squanchy.service.repository.firestore.TracksRepository
import net.squanchy.service.repository.injection.RepositoryModule
import net.squanchy.support.injection.ChecksumModule
import net.squanchy.support.injection.CurrentTimeModule

fun createApplicationComponent(application: Application): ApplicationComponent {
    return DaggerApplicationComponent.builder()
        .firestoreModule(FirestoreModule())
        .repositoryModule(RepositoryModule())
        .checksumModule(ChecksumModule())
        .applicationContextModule(ApplicationContextModule(application))
        .analyticsModule(AnalyticsModule(application))
        .remoteConfigModule(RemoteConfigModule())
        .tracksFilterModule(TracksFilterModule())
        .build()
}

@ApplicationLifecycle
@Component(
    modules = [
        ApplicationContextModule::class,
        FirestoreModule::class,
        ChecksumModule::class,
        RepositoryModule::class,
        AnalyticsModule::class,
        RemoteConfigModule::class,
        CurrentTimeModule::class,
        TracksFilterModule::class
    ]
)
interface ApplicationComponent {

    fun firestoreDbService(): FirestoreDbService

    fun firebaseAuthService(): FirebaseAuthService

    fun eventRepository(): EventRepository

    fun speakerRepository(): SpeakerRepository

    fun tracksRepository(): TracksRepository

    fun tracksFilter(): TracksFilter

    fun analytics(): Analytics

    fun remoteConfig(): RemoteConfig

    fun application(): Application
}
