package net.squanchy.about

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_about.fossButton
import kotlinx.android.synthetic.main.activity_about.githubButton
import kotlinx.android.synthetic.main.activity_about.websiteButton
import kotlinx.android.synthetic.main.activity_about.toolbar
import net.squanchy.R
import net.squanchy.support.view.enableLightNavigationBar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)
        enableLightNavigationBar(this)

        setupToolbar()

        val navigator = aboutComponent(this).navigator()

        websiteButton.setOnClickListener { navigator.toExternalUrl(SQUANCHY_WEBSITE) }
        githubButton.setOnClickListener { navigator.toExternalUrl(SQUANCHY_GITHUB) }
        fossButton.setOnClickListener { navigator.toFossLicenses() }
    }

    private fun setupToolbar() {
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
