package net.squanchy.signin

import android.app.Activity
import net.squanchy.injection.applicationComponent

fun signInComponent(activity: Activity) =
    DaggerSignInComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .signInModule(SignInModule())
        .build()
