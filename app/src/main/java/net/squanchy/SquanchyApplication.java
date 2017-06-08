package net.squanchy;

import android.app.Application;
import android.support.annotation.MainThread;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetUi;

import net.danlew.android.joda.JodaTimeAndroid;
import net.squanchy.analytics.Analytics;
import net.squanchy.fonts.TypefaceManager;
import net.squanchy.injection.ApplicationComponent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.fabric.sdk.android.Fabric;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class SquanchyApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);
        setupTracking();
        initializeFirebase();
        TypefaceManager.init();

        preloadProximityServiceToAllowForWarmingUp();
        preloadRemoteConfig();
    }

    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED") // Cannot handle the subscription here
    private void preloadRemoteConfig() {
        applicationComponent().remoteConfig()
                .fetchNow()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Timber.i("Remote config prefetched"),
                        throwable -> Timber.e(throwable, "Unable to preload the remote config")
                );
    }

    private void setupTracking() {
        setupFabric();

        Analytics analytics = applicationComponent().analytics();
        analytics.enableExceptionLogging();
        analytics.initializeStaticUserProperties();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void setupFabric() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.api_value_twitter_api_key),
                getString(R.string.api_value_twitter_secret)
        );

        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();

        Fabric.with(
                this,
                new Crashlytics.Builder().core(crashlyticsCore).build(),
                new TwitterCore(authConfig),
                new TweetUi()
        );
    }

    private void initializeFirebase() {
        // Calling initializeApp() should not be necessary, because Firebase uses the ContentProvider hack
        // (see FirebaseInitProvider). That means that, given we are not running multiple processes, we should
        // not need this. Unfortunately that's not the case; as of Firebase 10.2.0, the app sometimes crashes.
        if (FirebaseApp.initializeApp(this) == null) {
            Timber.e(new IllegalStateException("Initializing Firebase failed"));
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private void preloadProximityServiceToAllowForWarmingUp() {
        applicationComponent().service();
    }

    @MainThread
    public ApplicationComponent applicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = ApplicationComponent.Factory.create(this);
        }

        return applicationComponent;
    }
}
