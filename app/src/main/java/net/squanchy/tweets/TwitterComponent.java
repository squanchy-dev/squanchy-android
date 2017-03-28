package net.squanchy.tweets;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;
import net.squanchy.tweets.service.TwitterRepository;
import net.squanchy.tweets.service.TwitterService;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {TwitterModule.class, NavigationModule.class}, dependencies = ApplicationComponent.class)
interface TwitterComponent {

    TwitterRepository repository();

    TwitterService service();

    Navigator navigator();
}
