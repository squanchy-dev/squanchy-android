package net.squanchy.speaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_speaker_details.*
import net.squanchy.R
import net.squanchy.navigation.Navigator
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.config.DialogLayoutParameters
import net.squanchy.support.lang.getOrThrow
import timber.log.Timber

class SpeakerDetailsActivity : AppCompatActivity() {

    private val subscriptions = CompositeDisposable()
    private lateinit var service: SpeakerDetailsService
    private lateinit var navigator: Navigator

    private var speaker: Speaker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_speaker_details)

        DialogLayoutParameters.fullHeight(this)
            .applyTo(window)

        with(speakerDetailsComponent(this)) {
            service = service()
            navigator = navigator()
        }

        setupToolbar(toolbar)
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        subscriptions.clear()
        observeSpeakerFrom(intent)
    }

    override fun onStart() {
        super.onStart()

        observeSpeakerFrom(intent)
    }

    private fun observeSpeakerFrom(intent: Intent) {
        val speakerId = intent.getStringExtra(EXTRA_SPEAKER_ID)

        subscriptions.add(
            service.speaker(speakerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    ::onSpeakerRetrieved,
                    { e -> Timber.e(e, "Error retrieving the speaker") }
                )
        )
    }

    private fun onSpeakerRetrieved(speaker: Speaker) {
        speakerDetailsLayout.updateWith(speaker)

        this.speaker = speaker
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.speaker_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val twitterItem = menu.findItem(R.id.action_speaker_twitter)
        val websiteItem = menu.findItem(R.id.action_speaker_website)

        twitterItem.isVisible = speaker?.twitterUsername?.isDefined() ?: false
        websiteItem.isVisible = speaker?.personalUrl?.isDefined() ?: false

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_speaker_twitter -> {
                navigator.toTwitterProfile(assumeNonNull(speaker?.twitterUsername?.getOrThrow()))
                return true
            }
            R.id.action_speaker_website -> {
                navigator.toExternalUrl(assumeNonNull(speaker?.personalUrl?.getOrThrow()))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun <T> assumeNonNull(value: T?): T =
        value ?: throw IllegalStateException("Trying to access a null value assuming it was non-nullable")

    override fun onStop() {
        super.onStop()
        subscriptions.dispose()
    }

    companion object {

        private val EXTRA_SPEAKER_ID = SpeakerDetailsActivity::class.java.canonicalName + ".speaker_id"

        fun createIntent(context: Context, speakerId: String): Intent {
            val intent = Intent(context, SpeakerDetailsActivity::class.java)
            intent.putExtra(EXTRA_SPEAKER_ID, speakerId)
            return intent
        }
    }
}
