package net.squanchy.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.squanchy.R;
import net.squanchy.navigation.deeplink.DeepLinkRouter;
import net.squanchy.onboarding.Onboarding;
import net.squanchy.signin.SignInService;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class RoutingActivity extends AppCompatActivity {

    private static final int ONBOARDING_REQUEST_CODE = 2453;

    private DeepLinkRouter deepLinkRouter;
    private Navigator navigator;
    private Onboarding onboarding;
    private SignInService signInService;
    private FirstStartPersister firstStartPersister;

    private CompositeDisposable subscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscriptions = new CompositeDisposable();

        RoutingComponent component = RoutingInjector.obtain(this);
        deepLinkRouter = component.deepLinkRouter();
        navigator = component.navigator();
        onboarding = component.onboarding();
        signInService = component.signInService();
        firstStartPersister = component.firstStartPersister();
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscriptions.add(
                signInService.signInAnonymouslyIfNecessary()
                        .subscribe(this::onboardOrProceedToRouting, this::handleSignInError)
        );
    }

    private void handleSignInError(Throwable throwable) {
        Timber.e(throwable, "Error while signing in on routing");
        if (isFirstStart()) {
            // We likely have no data here and it'd be a horrible UX, so we show a warning instead
            // to let people know it won't work.
            Intent continuationIntent = createContinueIntentFrom(getIntent());
            navigator.toFirstStartWithNoNetwork(continuationIntent);
        } else {
            Toast.makeText(this, R.string.routing_sign_in_unexpected_error, Toast.LENGTH_LONG).show();
        }

        finish();
    }

    private Intent createContinueIntentFrom(Intent intent) {
        Intent continuationIntent = new Intent(intent);
        continuationIntent.removeCategory(Intent.CATEGORY_LAUNCHER);
        continuationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return continuationIntent;
    }

    private boolean isFirstStart() {
        return !firstStartPersister.hasBeenStartedAlready();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ONBOARDING_REQUEST_CODE) {
            handleOnboardingResult(resultCode);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleOnboardingResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            onboardOrProceedToRouting();
        } else {
            finish();
        }
    }

    private void onboardOrProceedToRouting() {
        subscriptions.add(
                Maybe.just(onboarding.nextPageToShow())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(page -> {
                            if (page.isPresent()) {
                                navigator.toOnboardingForResult(page.get(), ONBOARDING_REQUEST_CODE);
                            } else {
                                proceedTo(getIntent());
                            }
                        })
        );
    }

    private void proceedTo(Intent intent) {
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

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.clear();
    }
}
