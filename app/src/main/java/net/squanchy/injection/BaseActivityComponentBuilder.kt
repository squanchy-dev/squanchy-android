package net.squanchy.injection

import android.app.Activity
import dagger.BindsInstance

interface BaseActivityComponentBuilder<T> {
    fun applicationComponent(applicationComponent: ApplicationComponent): BaseActivityComponentBuilder<T>
    @BindsInstance
    fun activity(activity: Activity): BaseActivityComponentBuilder<T>

    fun build(): T
}