package net.squanchy.schedule.view

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import kotlinx.android.synthetic.main.item_schedule_event_other.view.*
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import org.joda.time.format.DateTimeFormat

class OtherEventItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.cardViewDefaultStyle
) : EventItemView(context, attrs, defStyleAttr) {

    override fun updateWith(event: Event, showRoom: Boolean, showDay: Boolean) {
        event.type.ensureSupported()

        timestamp.text = startTimeAsFormattedString(event)
        title.text = event.title
        illustration.setImageResource(illustrationFor(event.type))
    }

    private fun Event.Type.ensureSupported() {
        when (this) {
            Event.Type.COFFEE_BREAK,
            Event.Type.LUNCH,
            Event.Type.OTHER,
            Event.Type.REGISTRATION,
            Event.Type.SOCIAL -> return

            else -> throw IllegalArgumentException("Event with type $name is not supported by this view")
        }
    }

    private fun startTimeAsFormattedString(event: Event): String {
        val timeZone = event.timeZone
        val formatter = DateTimeFormat.shortTime()
            .withZone(timeZone)

        return formatter.print(event.startTime.toDateTime(timeZone))
    }

    @DrawableRes
    private fun illustrationFor(type: Event.Type) = when (type) {
        Event.Type.COFFEE_BREAK -> R.drawable.coffee_break
        Event.Type.LUNCH -> R.drawable.lunch
        Event.Type.REGISTRATION -> R.drawable.registration
        Event.Type.OTHER -> NO_DRAWABLE
        Event.Type.SOCIAL -> R.drawable.social
        else -> throw IllegalArgumentException("Type not supported: ${type.name}")
    }

    companion object {
        private const val NO_DRAWABLE = 0
    }
}
