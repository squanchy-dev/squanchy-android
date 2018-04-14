package net.squanchy.schedule.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.view.isVisible
import kotlinx.android.synthetic.main.item_schedule_event_talk.view.*
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.support.lang.getOrThrow
import org.joda.time.format.DateTimeFormat

class TalkEventItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.cardViewDefaultStyle
) : EventItemView(context, attrs, defStyleAttr) {

    private val timeFormatter = DateTimeFormat.shortTime()
    private val dateFormatter = DateTimeFormat.forPattern("EEE d")

    override fun updateWith(event: Event, showRoom: Boolean, showDay: Boolean, showFavorite: Boolean) {
        ensureSupportedType(event.type)

        timestamp.text = startTimeAsFormattedString(event, showDay)
        title.text = event.title
        room.apply {
            text = event.place.orNull().toPlaceLabel()
            isVisible = showRoom
        }

        if (event.experienceLevel.isDefined()) {
            experience_level.setExperienceLevel(event.experienceLevel.getOrThrow())
            experience_level.isVisible = true
        } else {
            experience_level.isVisible = false
        }

        speaker_container.visibility = if (event.speakers.isEmpty()) View.GONE else View.VISIBLE
        speaker_container.updateWith(event.speakers, null)

        favoriteIcon.isVisible = if (showFavorite) event.favorite else false
    }

    private fun Place?.toPlaceLabel(): CharSequence? = if (this != null) " • ${this.name}" else null

    private fun ensureSupportedType(type: Event.Type) {
        when (type) {
            Event.Type.TALK, Event.Type.KEYNOTE, Event.Type.WORKSHOP -> return
            else -> throw IllegalArgumentException("Event with type ${type.name} is not supported by this view")
        }
    }

    private fun startTimeAsFormattedString(event: Event, showDay: Boolean): String {
        val timeZone = event.timeZone
        val startDateTime = event.startTime.toDateTime(timeZone)

        // Note: the space at the end of the template string is not a typo, it separates the date from the time
        val formattedDate = if (showDay) "${dateFormatter.withZone(timeZone).print(startDateTime)} " else ""
        val formattedTime = timeFormatter.withZone(timeZone).print(startDateTime)

        return "$formattedDate$formattedTime"
    }
}
