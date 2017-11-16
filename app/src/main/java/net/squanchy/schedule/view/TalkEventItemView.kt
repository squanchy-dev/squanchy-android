package net.squanchy.schedule.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import net.squanchy.R
import net.squanchy.eventdetails.widget.ExperienceLevelView
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.lang.Optional
import net.squanchy.support.widget.SpeakerView
import org.joda.time.format.DateTimeFormat

class TalkEventItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = R.attr.cardViewDefaultStyle) :
        EventItemView(context, attrs, defStyleAttr) {

    private lateinit var titleView: TextView
    private lateinit var timestampView: TextView
    private lateinit var experienceLevelView: ExperienceLevelView
    private lateinit var speakerView: SpeakerView

    override fun onFinishInflate() {
        super.onFinishInflate()

        timestampView = findViewById(R.id.timestamp)
        titleView = findViewById(R.id.title)
        experienceLevelView = findViewById(R.id.experience_level)
        speakerView = findViewById(R.id.speaker_container)
    }

    override fun updateWith(event: Event) {
        ensureSupportedType(event.type)

        timestampView.text = startTimeAsFormattedString(event)
        titleView.text = event.title

        if (event.experienceLevel.isPresent) {
            experienceLevelView.setExperienceLevel(event.experienceLevel.get())
            experienceLevelView.visibility = View.VISIBLE
        } else {
            experienceLevelView.visibility = View.INVISIBLE
        }

        speakerView.visibility = if (event.speakers.isEmpty()) View.GONE else View.VISIBLE
        speakerView.updateWith(event.speakers, Optional.absent<SpeakerView.OnSpeakerClickListener>())
    }

    private fun startTimeAsFormattedString(event: Event): String {
        val formatter = DateTimeFormat.shortTime()
            .withZone(event.timeZone)

        return formatter.print(event.startTime.toDateTime())
    }

    private fun ensureSupportedType(type: Event.Type) {
        if (type === Event.Type.TALK || type === Event.Type.KEYNOTE) {
            return
        }
        throw IllegalArgumentException("Event with type ${type.name} is not supported by this view")
    }
}
