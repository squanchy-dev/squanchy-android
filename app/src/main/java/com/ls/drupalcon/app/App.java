package com.ls.drupalcon.app;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import com.crashlytics.android.Crashlytics;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.R;
import com.ls.drupalcon.model.AppDatabaseInfo;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.database.LAPIDBRegister;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (!BuildConfig.DEBUG) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(
                    getString(R.string.api_value_twitter_api_key),
                    getString(R.string.api_value_twitter_secret));
//        }

        mContext = getApplicationContext();

        LAPIDBRegister.getInstance().register(mContext, new AppDatabaseInfo(mContext));
        PreferencesManager.initializeInstance(mContext);
        Model.instance(mContext);
        DrupalImageView.setupSharedClient(new DrupalClient(null, Model.instance().createNewQueue(getApplicationContext()), BaseRequest.RequestFormat.JSON, null));
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
    }

    public static Context getContext() {
        return mContext;
    }

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(R.xml.global_tracker);
    }
}
