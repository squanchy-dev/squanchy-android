package com.ls.drupalcon.app;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import com.crashlytics.android.Crashlytics;
import com.ls.drupal.DrupalClient;
import com.ls.drupalcon.BuildConfig;
import com.ls.drupalcon.model.AppDatabaseInfo;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.database.LAPIDBRegister;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.ls.utils.ApplicationConfig;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class App extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Mxl1GoGSM98T3jTIWdlUuqXmh";
    private static final String TWITTER_SECRET = "UM74rykaGhxPhhKED2KxJrd6zGBLNWgVsGdlzjdSwSNqLTiyqY";

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (!BuildConfig.DEBUG) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
//        }

        mContext = getApplicationContext();

        LAPIDBRegister.getInstance().register(mContext, new AppDatabaseInfo(mContext));
        PreferencesManager.initializeInstance(mContext);
        Model.instance(mContext);
        DrupalImageView.setupSharedClient(new DrupalClient(null, Model.instance().createNewQueue(getApplicationContext()), BaseRequest.RequestFormat.JSON, null));
    }

    public static Context getContext() {
        return mContext;
    }

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(ApplicationConfig.PROPERTY_ID);
    }
}
