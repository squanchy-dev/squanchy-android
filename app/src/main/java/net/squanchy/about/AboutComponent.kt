package net.squanchy.about

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.BaseActivityComponentBuilder
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

fun aboutComponent(activity: AppCompatActivity): AboutComponent =
    DaggerAboutComponent.builder()
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [NavigationModule::class], dependencies = [ApplicationComponent::class])
interface AboutComponent {

    fun navigator(): Navigator

    @Component.Builder
    interface Builder : BaseActivityComponentBuilder<AboutComponent>
}
