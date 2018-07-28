package net.squanchy.tweets

import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import net.squanchy.injection.ActivityContextModule
import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.injection.applicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.tweets.service.TwitterService

internal fun twitterComponent(activity: AppCompatActivity): TwitterComponent {
    return DaggerTwitterComponent.builder()
        .applicationComponent(activity.applicationComponent)
        .activityContextModule(ActivityContextModule(activity))
        .twitterModule(TwitterModule())
        .navigationModule(NavigationModule())
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
}
