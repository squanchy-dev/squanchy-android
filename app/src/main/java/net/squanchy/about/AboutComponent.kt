package net.squanchy.about

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

fun aboutComponent(activity: AppCompatActivity) =
    DaggerAboutComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()

@ActivityLifecycle
@Component(modules = [NavigationModule::class])
interface AboutComponent {

    fun navigator(): Navigator
}
