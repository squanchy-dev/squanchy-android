package net.squanchy.about

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import net.squanchy.R
import net.squanchy.support.view.enableLightNavigationBar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)
        enableLightNavigationBar(this)

        setupToolbar()

        val navigator = aboutComponent(this).navigator()

        findViewById<View>(R.id.website_button).setOnClickListener { navigator.toExternalUrl(SQUANCHY_WEBSITE) }
        findViewById<View>(R.id.github_button).setOnClickListener { navigator.toExternalUrl(SQUANCHY_GITHUB) }
        findViewById<View>(R.id.foss_button).setOnClickListener { navigator.toFossLicenses() }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val SQUANCHY_WEBSITE = "https://squanchy.net"
        private const val SQUANCHY_GITHUB = "https://github.com/rock3r/squanchy"
    }
}
