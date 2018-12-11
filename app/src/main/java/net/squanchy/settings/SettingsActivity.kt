package net.squanchy.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_settings.*
import net.squanchy.R
import net.squanchy.signin.SignInService
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {

    private lateinit var signInService: SignInService
    private lateinit var subscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        setupToolbar()

        @Suppress("DEPRECATION") // We need to use the platform Fragments because of PreferenceFragment
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()

        val component = settingsActivityComponent(this)
        signInService = component.signInService()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onStart() {
        super.onStart()

        subscription = signInService.currentUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = settingsHeaderLayout::updateWith,
                onError = ::logError
            )
    }

    private fun logError(error: Throwable) {
        Timber.e(error, "Error in SettingsActivity: ${error.javaClass}")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        subscription.dispose()
    }
}
