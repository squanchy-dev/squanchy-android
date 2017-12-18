package net.squanchy

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import net.danlew.android.joda.JodaTimeAndroid
import net.squanchy.injection.createApplicationComponent
import timber.log.Timber

class SquanchyApplication : Application() {

    val applicationComponent by lazy { createApplicationComponent(this) }

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)
        setupTracking()
        initializeTwitter()
        initializeFirebase()

        preloadRemoteConfig()
    }

    private fun preloadRemoteConfig() {
        applicationComponent.remoteConfig()
            .fetchNow()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { Timber.i("Remote config prefetched") },
                    { throwable -> Timber.e(throwable, "Unable to preload the remote config") }
            )
    }

    private fun setupTracking() {
        initializeFabric()

        with(applicationComponent.analytics()) {
            enableExceptionLogging()
            initializeStaticUserProperties()
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initializeFabric() {
        val crashlyticsCore = CrashlyticsCore.Builder()
            .disabled(BuildConfig.DEBUG)
            .build()

        val crashlytics = Crashlytics.Builder()
            .core(crashlyticsCore)
            .build()

        Fabric.with(this, crashlytics)
    }

    private fun initializeTwitter() {
        val authConfig = TwitterAuthConfig(
                getString(R.string.api_value_twitter_api_key),
                getString(R.string.api_value_twitter_secret)
        )

        val twitterConfig = TwitterConfig.Builder(this)
            .twitterAuthConfig(authConfig)
            // TODO .logger(new TimberLogger())
            .debug(BuildConfig.DEBUG)
            .build()

        Twitter.initialize(twitterConfig)
    }

    private fun initializeFirebase() {
        // Calling initializeApp() should not be necessary, because Firebase uses the ContentProvider hack
        // (see FirebaseInitProvider). That means that, given we are not running multiple processes, we should
        // not need this. Unfortunately that's not the case; as of Firebase 10.2.0, the app sometimes crashes.
        if (FirebaseApp.initializeApp(this) == null) {
            Timber.e(IllegalStateException("Initializing Firebase failed"))
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
