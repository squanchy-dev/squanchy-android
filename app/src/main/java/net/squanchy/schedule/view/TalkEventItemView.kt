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

    override fun updateWith(event: Event, showRoom: Boolean) {
        ensureSupportedType(event.type)

        timestamp.text = startTimeAsFormattedString(event)
        title.text = event.title
        room.apply {
            text = event.place.orNull().toPlaceLabel()
            isVisible = showRoom
        }

        if (event.experienceLevel.isDefined()) {
            experience_level.setExperienceLevel(event.experienceLevel.getOrThrow())
            experience_level.visibility = View.VISIBLE
        } else {
            experience_level.visibility = View.INVISIBLE
        }

        speaker_container.visibility = if (event.speakers.isEmpty()) View.GONE else View.VISIBLE
        speaker_container.updateWith(event.speakers, null)
    }

    private fun Place?.toPlaceLabel(): CharSequence? = if (this != null) " • ${this.name}" else null

    private fun ensureSupportedType(type: Event.Type) {
        when (type) {
            Event.Type.TALK, Event.Type.KEYNOTE, Event.Type.WORKSHOP -> return
            else -> throw IllegalArgumentException("Event with type ${type.name} is not supported by this view")
        }
    }

    private fun startTimeAsFormattedString(event: Event): String {
        val formatter = DateTimeFormat.shortTime()
            .withZone(event.timeZone)

        return formatter.print(event.startTime.toDateTime())
    }
}
