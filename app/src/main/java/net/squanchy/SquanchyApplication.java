package net.squanchy;

import android.app.Application;
import android.support.annotation.MainThread;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import net.danlew.android.joda.JodaTimeAndroid;
import net.squanchy.analytics.Analytics;
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
        initializeTwitter();
        initializeFirebase();

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
        initializeFabric();

        Analytics analytics = applicationComponent().analytics();
        analytics.enableExceptionLogging();
        analytics.initializeStaticUserProperties();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initializeFabric() {
        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();

        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(crashlyticsCore)
                .build();

        Fabric.with(this, crashlytics);
    }

    private void initializeTwitter() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.api_value_twitter_api_key),
                getString(R.string.api_value_twitter_secret)
        );

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                // TODO .logger(new TimberLogger())
                .debug(BuildConfig.DEBUG)
                .build();

        Twitter.initialize(twitterConfig);
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

    @MainThread
    public ApplicationComponent applicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = ApplicationComponent.Factory.create(this);
        }

        return applicationComponent;
    }
}
