package net.squanchy.analytics;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.squanchy.proximity.ProximityProvider;
import net.squanchy.service.proximity.injection.ProximityModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ProximityModule.class)
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
    ProximityAnalytics proximityAnalytics(Application application, ProximityProvider proximityProvider) {
        return new ProximityAnalytics(application, proximityProvider);
    }

    @Provides
    Analytics analytics(FirebaseAnalytics firebaseAnalytics, Crashlytics crashlytics, ProximityAnalytics proximityAnalitics) {
        return new Analytics(firebaseAnalytics, crashlytics, proximityAnalitics);
    }
}
