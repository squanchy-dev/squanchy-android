package net.squanchy.settings

import android.app.Activity
import android.support.v7.app.AppCompatActivity

import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.signin.SignInModule

internal fun settingsFragmentComponent(activity: AppCompatActivity): SettingsFragmentComponent {
    return DaggerSettingsFragmentComponent.builder()
        .activityContextModule(ActivityContextModule(activity))
        .applicationComponent(activity.applicationComponent)
        .navigationModule(NavigationModule())
        .signInModule(SignInModule())
        .build()
}

internal fun settingsActivityComponent(activity: Activity): SettingsActivityComponent {
    return DaggerSettingsActivityComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .signInModule(SignInModule())
        .build()
}