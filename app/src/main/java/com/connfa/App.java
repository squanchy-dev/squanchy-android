package com.connfa;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.ls.drupal.DrupalClient;
import com.connfa.model.AppDatabaseInfo;
import com.connfa.model.Model;
import com.connfa.model.PreferencesManager;
import com.connfa.model.database.LAPIDBRegister;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        if (!BuildConfig.DEBUG) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.api_value_twitter_api_key),
                getString(R.string.api_value_twitter_secret));
//        }

        LAPIDBRegister.getInstance().register(this, new AppDatabaseInfo(this));
        PreferencesManager.initializeInstance(this);
        Model.instance(this);
        DrupalClient client = new DrupalClient(
                null,
                Model.instance().createNewQueue(getApplicationContext()),
                BaseRequest.RequestFormat.JSON,
                null
        );
        DrupalImageView.setupSharedClient(client);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
    }

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(R.xml.global_tracker);
    }
}
