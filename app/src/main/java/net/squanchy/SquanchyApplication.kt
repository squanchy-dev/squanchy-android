package net.squanchy

import android.annotation.SuppressLint
import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.database.FirebaseDatabase
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
        initializeFirebase()
    }

    private fun setupTracking() {
        initializeFabric()

        with(applicationComponent.analytics()) {
            enableExceptionLogging()
            initializeStaticUserProperties()
            trackUserInitiallyNotLoggedIn()
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

    private fun initializeFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        preloadRemoteConfig()
    }

    @SuppressLint("CheckResult") // This is a fire-and-forget operation
    private fun preloadRemoteConfig() {
        applicationComponent.remoteConfig()
            .fetchNow()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Timber.i("Remote config prefetched") },
                { throwable -> Timber.e(throwable, "Unable to preload the remote config") }
            )
    }
}
