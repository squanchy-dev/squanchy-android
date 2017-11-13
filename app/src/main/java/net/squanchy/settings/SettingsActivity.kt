package net.squanchy.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import net.squanchy.R
import net.squanchy.settings.view.SettingsHeaderLayout
import net.squanchy.signin.SignInService

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class SettingsActivity : AppCompatActivity() {

    private lateinit var signInService: SignInService
    private lateinit var headerLayout: SettingsHeaderLayout
    private lateinit var subscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        headerLayout = findViewById(R.id.settings_header)

        setupToolbar()

        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()

        val component = settingsActivityComponent(this)
        signInService = component.signInService()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onStart() {
        super.onStart()

        subscription = signInService.currentUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(headerLayout::updateWith)
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
