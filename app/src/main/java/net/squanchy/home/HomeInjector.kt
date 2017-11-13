package net.squanchy.home

import android.support.v7.app.AppCompatActivity

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent

internal fun homeComponent(activity: AppCompatActivity): HomeComponent {
    return DaggerHomeComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activityContextModule(ActivityContextModule(activity))
        .build()
}
