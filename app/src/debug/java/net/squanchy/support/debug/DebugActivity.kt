package net.squanchy.support.debug

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import arrow.core.Option
import com.google.android.material.snackbar.Snackbar
import net.squanchy.R
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.notification.NotificationCreator
import net.squanchy.notification.NotificationsIntentService
import net.squanchy.notification.Notifier
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.speaker.domain.view.Speaker
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import java.util.ArrayList
import java.util.Random

class DebugActivity : AppCompatActivity() {

    private lateinit var notificationCreator: NotificationCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        val buttonSingleNotification = findViewById<View>(R.id.button_test_single_notification)
        buttonSingleNotification.setOnClickListener { testSingleNotification() }

        val buttonMultipleNotifications = findViewById<View>(R.id.button_test_multiple_notifications)
        buttonMultipleNotifications.setOnClickListener { testMultipleNotifications() }

        val buttonService = findViewById<View>(R.id.button_test_service)
        buttonService.setOnClickListener { testService() }

        val buttonResetOnboarding = findViewById<View>(R.id.button_reset_onboarding)
        buttonResetOnboarding.setOnClickListener { resetOnboarding() }

        notificationCreator = NotificationCreator(this)
    }

    private fun testSingleNotification() {
        createAndNotifyTalksCount(1)
    }

    private fun testMultipleNotifications() {
        createAndNotifyTalksCount(3)
    }

    private fun createAndNotifyTalksCount(count: Int) {
        val events = (0 until count).map(::createTestEvent)

        val notifications = notificationCreator.createFrom(events)

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        val notifier = Notifier(notificationManagerCompat)
        notifier.showNotifications(notifications)
    }

    private fun createTestEvent(id: Int): Event {
        val start = LocalDateTime().plusMinutes(5)
        val end = LocalDateTime().plusMinutes(45)
        return Event(
            id = id.toString(),
            numericId = id.toLong(),
            startTime = start,
            endTime = end,
            title = "A very interesting talk",
            place = createPlace(),
            experienceLevel = Option(ExperienceLevel.ADVANCED),
            speakers = createTalkSpeakers(),
            type = Event.Type.TALK,
            favorite = true,
            description = Option.empty(),
            track = Option(createTrack()),
            timeZone = DateTimeZone.forID("Europe/Rome")
        )
    }

    private fun createPlace(): Option<Place> = Option(
        Place("1", "That room over there", Option.empty(), -1)
    )

    private fun createTalkSpeakers(): List<Speaker> {
        val speakers = ArrayList<Speaker>(2)
        speakers.add(
            Speaker(
                id = "1",
                numericId = 101L,
                name = "Ajeje Brazorf",
                bio = "An Android dev",
                companyName = Option.empty(),
                companyUrl = Option.empty(),
                personalUrl = Option.empty(),
                photoUrl = Option("https://yt3.ggpht.com/-d35Rq8vqvmE/AAAAAAAAAAAA/zy1VyiRTNec/s900-c-k-no-mo-rj-c0xffffff/photo.jpg"),
                twitterUsername = Option.empty()
            )
        )
        return speakers
    }

    private fun createTrack() = Track(
        "0",
        0,
        "UI",
        generateColor(),
        generateColor(),
        null
    )

    private fun generateColor(): String {
        val r = Random()
        val hex = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val s = CharArray(7)
        var n = r.nextInt(0x1000000)

        s[0] = '#'
        for (i in 1..6) {
            s[i] = hex[n and 0xf]
            n = n shr 4
        }
        return String(s)
    }

    private fun testService() {
        val serviceIntent = Intent(this, NotificationsIntentService::class.java)
        startService(serviceIntent)
    }

    private fun resetOnboarding() {
        OnboardingResetter(this).resetOnboarding()
        Snackbar.make(findViewById<View>(R.id.debug_root), "It's daaaawnnnn", Snackbar.LENGTH_SHORT).show()
    }
}
