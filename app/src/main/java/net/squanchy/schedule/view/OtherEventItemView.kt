package net.squanchy.schedule.view

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import org.joda.time.format.DateTimeFormat

class OtherEventItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = R.attr.cardViewDefaultStyle
) : EventItemView(context, attrs, defStyleAttr) {

    private lateinit var titleView: TextView
    private lateinit var timestampView: TextView
    private lateinit var illustrationView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()

        timestampView = findViewById(R.id.timestamp)
        titleView = findViewById(R.id.title)
        illustrationView = findViewById(R.id.illustration)
    }

    override fun updateWith(event: Event) {
        ensureSupportedType(event.type)

        timestampView.text = startTimeAsFormattedString(event)
        titleView.text = event.title

        illustrationView.setImageResource(illustrationFor(event.type))
    }

    private fun ensureSupportedType(type: Event.Type) {
        if (type === Event.Type.COFFEE_BREAK
                || type === Event.Type.LUNCH
                || type === Event.Type.OTHER
                || type === Event.Type.REGISTRATION
                || type === Event.Type.SOCIAL) {
            return
        }
        throw IllegalArgumentException("Event with type ${type.name} is not supported by this view")
    }

    private fun startTimeAsFormattedString(event: Event): String {
        val formatter = DateTimeFormat.shortTime()
            .withZone(event.timeZone)

        return formatter.print(event.startTime.toDateTime())
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
        private val NO_DRAWABLE = 0
    }
}
