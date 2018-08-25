package net.squanchy

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.database.FirebaseDatabase
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import net.danlew.android.joda.JodaTimeAndroid
import net.squanchy.injection.createApplicationComponent
import timber.log.Timber

class SquanchyApplication : Application() {

    val applicationComponent by lazy { createApplicationComponent(this) }

    private val subscriptions = CompositeDisposable()

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
            trackFirstStartUserNotLoggedIn()
            trackFirstStartNotificationsEnabled()
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
        preloadTracks()
    }

    private fun preloadRemoteConfig() {
        subscriptions += applicationComponent.remoteConfig()
            .fetchNow()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { Timber.i("Remote config prefetched") },
                onError = { throwable -> Timber.e(throwable, "Unable to preload the remote config") }
            )
    }

    private fun preloadTracks() {
        subscriptions += applicationComponent.tracksRepository()
            .tracks()
            .subscribeBy(
                onNext = { Timber.d("Tracks prefetched: $it") },
                onError = { throwable -> Timber.e(throwable, "Unable to preload the tracks") }
            )
    }

    override fun onTerminate() {
        super.onTerminate()
        // Best-effort to clear any outstanding subscriptions — mostly for RxLint's sake, as this method is not
        // guaranteed to be called (and, in fact, it's almost never). But that's ok as those subscription will
        // have long terminated (and unsubscribed) by the time the process is killed, and even if they hadn't,
        // we'd be killing the process anyway, which is the most extreme form of unsubscription ;)
        subscriptions.clear()
    }
}
