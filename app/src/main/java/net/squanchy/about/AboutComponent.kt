package net.squanchy.about

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator

fun aboutComponent(activity: AppCompatActivity): AboutComponent =
    DaggerAboutComponent.builder()
        .activity(activity)
        .build()

@ActivityLifecycle
@Component(modules = [NavigationModule::class])
interface AboutComponent {

    fun navigator(): Navigator

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): AboutComponent
    }
}
