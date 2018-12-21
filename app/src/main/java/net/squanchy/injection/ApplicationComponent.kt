package net.squanchy.injection

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.AnalyticsModule
import net.squanchy.remoteconfig.FeatureFlags
import net.squanchy.remoteconfig.RemoteConfig
import net.squanchy.remoteconfig.RemoteConfigModule
import net.squanchy.schedule.tracksfilter.TracksFilter
import net.squanchy.schedule.tracksfilter.TracksFilterModule
import net.squanchy.search.algolia.AlgoliaModule
import net.squanchy.search.algolia.AlgoliaSearchEngine
import net.squanchy.service.firebase.FirestoreDbService
import net.squanchy.service.firebase.injection.FirestoreModule
import net.squanchy.service.repository.AuthService
import net.squanchy.service.repository.EventRepository
import net.squanchy.service.repository.SpeakerRepository
import net.squanchy.service.repository.TracksRepository
import net.squanchy.service.repository.injection.RepositoryModule
import net.squanchy.support.checksum.ChecksumModule
import net.squanchy.support.injection.CurrentTimeModule
import net.squanchy.support.system.CurrentTime

fun createApplicationComponent(application: Application): ApplicationComponent {
    return DaggerApplicationComponent.builder()
        .application(application)
        .build()
}

@ApplicationLifecycle
@Component(
    modules = [
        AlgoliaModule::class,
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

    fun firebaseAuthService(): AuthService

    fun eventRepository(): EventRepository

    fun speakerRepository(): SpeakerRepository

    fun tracksRepository(): TracksRepository

    fun tracksFilter(): TracksFilter

    fun analytics(): Analytics

    fun remoteConfig(): RemoteConfig

    fun featureFlags(): FeatureFlags

    fun application(): Application

    fun algoliaSearchEngine(): AlgoliaSearchEngine

    fun currentTime(): CurrentTime

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }
}
