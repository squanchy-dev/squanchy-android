package com.connfa;

import android.app.Application;

import com.connfa.analytics.Analytics;
import com.connfa.model.AppDatabaseInfo;
import com.connfa.model.Model;
import com.connfa.model.database.LAPIDBRegister;
import com.crashlytics.android.Crashlytics;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class ConnfaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setupTracking();

        LAPIDBRegister.getInstance().register(this, new AppDatabaseInfo(this));

        Model.createInstance(this);

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
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
    }
}
