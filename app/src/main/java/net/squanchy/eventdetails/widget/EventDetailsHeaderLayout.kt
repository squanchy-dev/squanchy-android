package net.squanchy.eventdetails.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_event_details.view.*
import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.widget.SpeakerView

class EventDetailsHeaderLayout(context: Context, attrs: AttributeSet) : AppBarLayout(context, attrs) {

    internal fun updateWith(event: Event, listener: SpeakerView.OnSpeakerClickListener) {
        titleTextView.text = event.title
        titleTextView.isVisible = true

        speakerDetailsView.visibility = if (event.speakers.isEmpty()) View.GONE else View.VISIBLE
        speakerDetailsView.updateWith(event.speakers, listener)
    }
}
