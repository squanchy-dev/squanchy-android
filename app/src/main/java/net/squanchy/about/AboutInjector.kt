package net.squanchy.about

import android.support.v7.app.AppCompatActivity

import net.squanchy.injection.ActivityContextModule
import net.squanchy.navigation.NavigationModule

fun aboutComponent(activity: AppCompatActivity): AboutComponent {
    return DaggerAboutComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .navigationModule(NavigationModule())
        .build()
}