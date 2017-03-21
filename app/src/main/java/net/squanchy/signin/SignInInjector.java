package net.squanchy.signin;

import android.app.Activity;

import net.squanchy.injection.ApplicationInjector;

final class SignInInjector {

    private SignInInjector() {
        // no instances
    }

    public static SignInComponent obtain(Activity activity) {
        return DaggerSignInComponent.builder()
                .applicationComponent(ApplicationInjector.obtain(activity))
                .signInModule(new SignInModule())
                .build();
    }
}
