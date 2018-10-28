package net.squanchy.favorites

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.analytics.Analytics
import net.squanchy.injection.BaseActivityComponentBuilder
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.remoteconfig.FeatureFlags

internal fun favoritesComponent(activity: AppCompatActivity): FavoritesComponent =
    DaggerFavoritesComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [FavoritesModule::class, NavigationModule::class], dependencies = [ApplicationComponent::class])
internal interface FavoritesComponent {

    fun favoritesService(): FavoritesService

    fun navigator(): Navigator

    fun analytics(): Analytics

    fun featureFlags(): FeatureFlags

    @Component.Builder
    interface Builder : BaseActivityComponentBuilder<FavoritesComponent>
}
