package net.squanchy;

import android.app.Application;
import android.support.annotation.MainThread;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetUi;

import net.danlew.android.joda.JodaTimeAndroid;
import net.squanchy.analytics.Analytics;
import net.squanchy.fonts.TypefaceManager;
import net.squanchy.injection.ApplicationComponent;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class SquanchyApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);
        setupTracking();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        TypefaceManager.init();
    }

    private void setupTracking() {
        setupFabric();

        Analytics analytics = Analytics.from(this);
        analytics.enableActivityLifecycleLogging();
        analytics.enableExceptionLogging();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void setupFabric() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.api_value_twitter_api_key),
                getString(R.string.api_value_twitter_secret)
        );
        Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig), new TweetUi());
    }

    @MainThread
    public ApplicationComponent applicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = ApplicationComponent.Factory.create(this);
        }

        return applicationComponent;
    }
}
