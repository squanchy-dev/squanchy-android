package net.squanchy.support.view

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View

@TargetApi(Build.VERSION_CODES.O)
fun enableLightNavigationBar(activity: Activity) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }

    activity.window.apply {
        navigationBarColor = -0x111112
        decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
}
