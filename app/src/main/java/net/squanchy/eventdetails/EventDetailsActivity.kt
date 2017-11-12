package net.squanchy.eventdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import net.squanchy.R
import net.squanchy.eventdetails.EventDetailsService.*
import net.squanchy.eventdetails.widget.EventDetailsCoordinatorLayout
import net.squanchy.navigation.Navigator
import net.squanchy.notification.NotificationsIntentService
import net.squanchy.schedule.domain.view.Event
import net.squanchy.speaker.domain.view.Speaker

class EventDetailsActivity : AppCompatActivity() {

    private val subscriptions = CompositeDisposable()

    private lateinit var service: EventDetailsService
    private lateinit var coordinatorLayout: EventDetailsCoordinatorLayout

    private lateinit var navigator: Navigator
    private lateinit var eventId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event_details)

        setupToolbar()

        with(eventDetailsComponent(this)) {
            service = service()
            navigator = navigator()
        }

        coordinatorLayout = findViewById(R.id.event_details_root)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        eventId = intent.getStringExtra(EXTRA_EVENT_ID)

        subscribeToEvent(eventId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE_SIGNIN) {
            subscribeToEvent(eventId)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun subscribeToEvent(eventId: String) {
        subscriptions.add(
                service.event(eventId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { event -> coordinatorLayout.updateWith(event, onEventDetailsClickListener(event)) }
        )
    }

    private fun onEventDetailsClickListener(event: Event): EventDetailsCoordinatorLayout.OnEventDetailsClickListener {
        return object : EventDetailsCoordinatorLayout.OnEventDetailsClickListener {
            override fun onSpeakerClicked(speaker: Speaker) {
                navigate().toSpeakerDetails(speaker.id)
            }

            override fun onFavoriteClick() {
                subscriptions.add(
                        service.toggleFavorite(event).subscribe { result ->
                            if (result === FavoriteResult.MUST_AUTHENTICATE) {
                                requestSignIn()
                            } else {
                                triggerNotificationService()
                            }
                        }
                )
            }
        }
    }

    private fun requestSignIn() {
        navigate().toSignInForResult(REQUEST_CODE_SIGNIN)
        unsubscribeFromUpdates()
    }

    private fun triggerNotificationService() {
        val serviceIntent = Intent(this, NotificationsIntentService::class.java)
        startService(serviceIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.event_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_search -> { navigate().toSearch(); true }
            android.R.id.home -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigate(): Navigator = navigator

    override fun onStop() {
        super.onStop()

        unsubscribeFromUpdates()
    }

    private fun unsubscribeFromUpdates() {
        subscriptions.clear()
    }

    companion object {

        private val EXTRA_EVENT_ID = EventDetailsActivity::class.java.canonicalName + ".event_id"
        private const val REQUEST_CODE_SIGNIN = 1235

        fun createIntent(context: Context, eventId: String): Intent {
            return Intent(context, EventDetailsActivity::class.java).apply {
                putExtra(EXTRA_EVENT_ID, eventId)
            }
        }
    }
}
