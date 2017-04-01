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
    private SignInService signInService;
    private FirstStartPersister firstStartPersister;

    private Disposable subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RoutingComponent component = RoutingInjector.obtain(this);
        deepLinkRouter = component.deepLinkRouter();
        navigator = component.navigator();
        signInService = component.signInService();
        firstStartPersister = component.firstStartPersister();
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscription = signInService.signInAnonymouslyIfNecessary()
                .subscribe(this::proceedToRouting, this::handleSignInError);
    }

    private void proceedToRouting() {
        Intent intent = getIntent();
        if (deepLinkRouter.hasDeepLink(intent)) {
            String intentUriString = intent.getDataString();
            Timber.i("Deeplink detected, navigating to \"%s\"", intentUriString);
            deepLinkRouter.navigateTo(intentUriString);
        } else {
            navigator.toHomePage();
        }

        firstStartPersister.storeHasBeenStarted();
        finish();
    }

    private void handleSignInError(Throwable throwable) {
        Timber.e(throwable, "Error while signing in on routing");
        if (isFirstStart()) {
            // We likely have no data here and it'd be a horrible UX, so we show a warning instead
            // to let people know it won't work.
            navigator.toFirstStartWithNoNetwork();
            finish();
        }
    }

    private boolean isFirstStart() {
        return !firstStartPersister.hasBeenStartedAlready();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.dispose();
    }
}
