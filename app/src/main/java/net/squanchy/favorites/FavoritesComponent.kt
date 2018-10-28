package net.squanchy.favorites

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.FeatureFlags

internal fun favoritesComponent(activity: AppCompatActivity): FavoritesComponent {
    return DaggerFavoritesComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()
}

@ActivityLifecycle
@Component(modules = [FavoritesModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface FavoritesComponent {

    fun favoritesService(): FavoritesService

    fun navigator(): Navigator

    fun analytics(): Analytics

    fun featureFlags(): FeatureFlags

    @Component.Builder
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): FavoritesComponent
    }
}
