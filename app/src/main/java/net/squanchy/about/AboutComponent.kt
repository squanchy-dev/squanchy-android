package net.squanchy.about

import android.support.v7.app.AppCompatActivity
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

import dagger.Component
import net.squanchy.injection.ActivityContextModule

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
