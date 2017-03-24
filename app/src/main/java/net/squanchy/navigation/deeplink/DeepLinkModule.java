package net.squanchy.navigation.deeplink;

import android.content.Context;

import net.squanchy.R;
import net.squanchy.injection.ActivityContextModule;
import net.squanchy.navigation.NavigationModule;
import net.squanchy.navigation.Navigator;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ActivityContextModule.class, NavigationModule.class})
public class DeepLinkModule {

    @Provides
    String deepLinkScheme(Context context) {
        return context.getString(R.string.deeplink_scheme);
    }

    @Provides
    DeepLinkNavigator deepLinkNavigator(Navigator navigator) {
        return new DeepLinkNavigator(navigator);
    }

    @Provides
    NavigationStrategyFactory navigationStrategyFactory(DeepLinkNavigator deepLinkNavigator) {
        return new NavigationStrategyFactory(deepLinkNavigator);
    }

    @Provides
    DeepLinkRouter deepLinkRouter(String deepLinkScheme, NavigationStrategyFactory strategyFactory, DeepLinkNavigator navigator) {
        return new DeepLinkRouter(deepLinkScheme, strategyFactory, navigator);
    }
}
