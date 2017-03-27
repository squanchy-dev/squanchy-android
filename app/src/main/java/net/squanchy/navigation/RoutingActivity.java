package net.squanchy.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.deeplink.DeepLinkRouter;
import net.squanchy.signin.SignInService;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class RoutingActivity extends TypefaceStyleableActivity {

    private DeepLinkRouter deepLinkRouter;
    private Navigator navigator;
    private Disposable subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RoutingComponent component = RoutingInjector.obtain(this);
        deepLinkRouter = component.deepLinkRouter();
        navigator = component.navigator();

        SignInService signInService = component.signInService();
        subscription = signInService.signInAnonymouslyIfNecessary().subscribe();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.dispose();
    }
}
