package net.squanchy.eventdetails.widget

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.lang.Optional
import net.squanchy.support.widget.SpeakerView

class EventDetailsHeaderLayout(context: Context, attrs: AttributeSet) : AppBarLayout(context, attrs) {

    private lateinit var titleView: TextView
    private lateinit var speakerView: SpeakerView

    override fun onFinishInflate() {
        super.onFinishInflate()

        titleView = findViewById(R.id.title)
        speakerView = findViewById(R.id.speaker_container)
    }

    internal fun updateWith(event: Event, listener: SpeakerView.OnSpeakerClickListener) {
        titleView.text = event.title
        titleView.visibility = View.VISIBLE

        speakerView.visibility = if (event.speakers.isEmpty()) View.GONE else View.VISIBLE
        speakerView.updateWith(event.speakers, Optional.of<SpeakerView.OnSpeakerClickListener>(listener))
    }
}
