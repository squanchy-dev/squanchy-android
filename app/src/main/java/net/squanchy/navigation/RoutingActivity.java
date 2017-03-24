package net.squanchy.navigation;

import android.content.Intent;

import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.deeplink.DeepLinkRouter;

import timber.log.Timber;

public class RoutingActivity extends TypefaceStyleableActivity {

    @Override
    protected void onStart() {
        super.onStart();

        RoutingComponent component = RoutingInjector.obtain(this);
        DeepLinkRouter deepLinkRouter = component.deepLinkRouter();
        Navigator navigator = component.navigator();

        Intent intent = getIntent();
        if (deepLinkRouter.hasDeepLink(intent)) {
            String intentUriString = intent.getDataString();
            Timber.i("Deeplink detected, navigating to \"%s\"", intentUriString);
            deepLinkRouter.navigateTo(intentUriString);
        } else {
            navigator.toSchedule();
        }

        finish();
    }
}
