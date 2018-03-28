package net.squanchy.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_settings.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.signin.SignInService

class SettingsActivity : AppCompatActivity() {

    private lateinit var signInService: SignInService
    private lateinit var analytics: Analytics
    private lateinit var subscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        setupToolbar()

        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()

        val component = settingsActivityComponent(this)
        signInService = component.signInService()
        analytics = component.analytics()
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
            .subscribe(settingsHeaderLayout::updateWith)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != SettingsFragment.REQUEST_CODE_SIGNIN) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            analytics.trackUserLoggedInSettings()
        }
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
