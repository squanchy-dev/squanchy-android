package net.squanchy.analytics;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {

    private final Application application;

    public AnalyticsModule(Application application) {
        this.application = application;
    }

    @Provides
    FirebaseAnalytics firebaseAnalytics() {
        return FirebaseAnalytics.getInstance(application);
    }

    @Provides
    Crashlytics crashlytics() {
        return Crashlytics.getInstance();
    }

    @Provides
    Analytics analytics(FirebaseAnalytics firebaseAnalytics, Crashlytics crashlytics) {
        return new Analytics(firebaseAnalytics, crashlytics);
    }
}
