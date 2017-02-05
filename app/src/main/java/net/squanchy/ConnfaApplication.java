package net.squanchy;

import android.app.Application;
import android.support.annotation.MainThread;

import net.squanchy.analytics.Analytics;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.model.AppDatabaseInfo;
import net.squanchy.model.Model;
import net.squanchy.model.database.LAPIDBRegister;
import net.squanchy.service.api.ConnfaRepository;
import com.crashlytics.android.Crashlytics;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetUi;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class ConnfaApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        setupTracking();

        LAPIDBRegister.getInstance().register(this, new AppDatabaseInfo(this));

        ConnfaRepository repository = ConnfaRepository.newInstance(this);
        Model.createInstance(this, repository);

        DrupalClient client = new DrupalClient(
                null,
                Model.getInstance().createNewQueue(getApplicationContext()),
                BaseRequest.RequestFormat.JSON,
                null
        );

        DrupalImageView.setupSharedClient(client);
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
