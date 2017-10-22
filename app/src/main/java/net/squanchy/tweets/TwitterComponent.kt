package net.squanchy.tweets

import net.squanchy.injection.ActivityLifecycle
import net.squanchy.injection.ApplicationComponent
import net.squanchy.navigation.NavigationModule
import net.squanchy.navigation.Navigator
import net.squanchy.tweets.service.TwitterRepository
import net.squanchy.tweets.service.TwitterService

import dagger.Component

@ActivityLifecycle
@Component(
        modules = arrayOf(TwitterModule::class, NavigationModule::class),
        dependencies = arrayOf(ApplicationComponent::class)
)
internal interface TwitterComponent {

    fun repository(): TwitterRepository

    fun service(): TwitterService

    fun navigator(): Navigator
}
