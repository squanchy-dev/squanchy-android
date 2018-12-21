package net.squanchy.support.debug

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import arrow.core.Option
import com.google.android.material.snackbar.Snackbar
import net.squanchy.R
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.injection.createApplicationComponent
import net.squanchy.notification.NotificationCreator
import net.squanchy.notification.Notifier
import net.squanchy.notification.scheduleNotificationWork
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.schedule.domain.view.Track
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.system.FreezableCurrentTime
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Random

class DebugActivity : AppCompatActivity() {

    private lateinit var notificationCreator: NotificationCreator

    private lateinit var currentTime: FreezableCurrentTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        createApplicationComponent(application).also {
            currentTime = it.currentTime() as FreezableCurrentTime
        }

        val buttonSingleNotification = findViewById<View>(R.id.button_test_single_notification)
        buttonSingleNotification.setOnClickListener { testSingleNotification() }

        val buttonMultipleNotifications = findViewById<View>(R.id.button_test_multiple_notifications)
        buttonMultipleNotifications.setOnClickListener { testMultipleNotifications() }

        val buttonService = findViewById<View>(R.id.button_test_service)
        buttonService.setOnClickListener { testService() }

        val buttonResetOnboarding = findViewById<View>(R.id.button_reset_onboarding)
        buttonResetOnboarding.setOnClickListener { resetOnboarding() }

        notificationCreator = NotificationCreator(this)

        findViewById<Button>(R.id.freezeTime).setOnClickListener { freezeTime() }
        findViewById<Button>(R.id.unfreezeTime).setOnClickListener { unfreezeTime() }
        updateFrozenTimeLabel()
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
        val start = LocalDateTime.now().plusMinutes(5)
        val end = LocalDateTime.now().plusMinutes(45)
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
            timeZone = ZoneId.of("Europe/Rome")
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
        scheduleNotificationWork()
    }

    private fun resetOnboarding() {
        OnboardingResetter(this).resetOnboarding()
        Snackbar.make(findViewById<View>(R.id.debug_root), "It's daaaawnnnn", Snackbar.LENGTH_SHORT).show()
    }

    private fun freezeTime() {
        val now = currentTime.currentDateTime()
        pickDate(now)
    }

    private fun pickDate(now: ZonedDateTime) {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                pickTime(now, LocalDate.of(year, month.to1Based(), dayOfMonth))
            },
            now.year,
            now.monthValue.to0Based(),
            now.dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun Int.to1Based() = this + 1

    private fun Int.to0Based() = this - 1

    private fun pickTime(now: ZonedDateTime, frozenDate: LocalDate) {
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                freezeAt(frozenDate.atTime(hourOfDay, minute).atZone(ZoneId.systemDefault()))
            },
            now.hour,
            now.minute,
            true
        )

        timePickerDialog.show()
    }

    private fun freezeAt(frozenDateTime: ZonedDateTime) {
        currentTime.freeze(frozenDateTime)
        updateFrozenTimeLabel()
    }

    private fun unfreezeTime() {
        currentTime.unfreeze()
        updateFrozenTimeLabel()
    }

    private fun updateFrozenTimeLabel() {
        findViewById<TextView>(R.id.timeFrozenIndicator).text = if (currentTime.isTimeFrozen()) {
            val time = currentTime.currentDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a"))
            getString(R.string.debug_time_frozen_at, time)
        } else {
            getString(R.string.debug_time_not_frozen)
        }
    }
}
