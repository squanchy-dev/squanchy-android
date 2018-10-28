package net.squanchy.tweets

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.tweets.service.TwitterService

internal fun twitterComponent(activity: AppCompatActivity): TwitterComponent {
    return DaggerTwitterComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activity(activity)
        .build()
}

@ActivityLifecycle
@Component(
        modules = [(TwitterModule::class), (NavigationModule::class)],
        dependencies = [(ApplicationComponent::class)]
)
internal interface TwitterComponent {

    fun service(): TwitterService

    fun navigator(): Navigator

    @Component.Builder
    interface Builder {
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): TwitterComponent
    }
}
