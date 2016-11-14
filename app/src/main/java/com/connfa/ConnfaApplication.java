package com.connfa;

import android.app.Application;

import com.connfa.model.AppDatabaseInfo;
import com.connfa.model.Model;
import com.connfa.model.database.LAPIDBRegister;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class ConnfaApplication extends Application {

    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();

        LAPIDBRegister.getInstance().register(this, new AppDatabaseInfo(this));

        Model.createInstance(this);

        DrupalClient client = new DrupalClient(
                null,
                Model.getInstance().createNewQueue(getApplicationContext()),
                BaseRequest.RequestFormat.JSON,
                null
        );

        DrupalImageView.setupSharedClient(client);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.api_value_twitter_api_key),
                getString(R.string.api_value_twitter_secret)
        );
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));

        tracker = GoogleAnalytics
                .getInstance(this)
                .newTracker(R.xml.global_tracker);
    }

    public Tracker getTracker() {
        return tracker;
    }
}
