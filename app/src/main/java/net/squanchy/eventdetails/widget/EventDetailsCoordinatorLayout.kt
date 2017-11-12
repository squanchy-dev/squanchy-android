package net.squanchy.eventdetails.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View

import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Event.Type
import net.squanchy.support.widget.SpeakerView

class EventDetailsCoordinatorLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var headerLayout: EventDetailsHeaderLayout
    private lateinit var detailsLayout: EventDetailsLayout

    override fun onFinishInflate() {
        super.onFinishInflate()

        floatingActionButton = findViewById(R.id.favorite_fab)
        headerLayout = findViewById(R.id.event_details_header)
        detailsLayout = findViewById(R.id.event_details)
    }

    internal fun updateWith(event: Event, listener: OnEventDetailsClickListener) {
        headerLayout.updateWith(event, listener)
        detailsLayout.updateWith(event)

        if (canBeFavorited(event)) {
            floatingActionButton.setImageResource(
                    if (event.favorited)
                        R.drawable.ic_favorite_filled
                    else
                        R.drawable.ic_favorite_empty
            )
            floatingActionButton.setOnClickListener { listener.onFavoriteClick() }
            floatingActionButton.visibility = View.VISIBLE
        } else {
            floatingActionButton.visibility = View.GONE
        }
    }

    private fun canBeFavorited(event: Event): Boolean {
        val type = event.type
        return type === Type.TALK || type === Type.KEYNOTE
    }

    internal interface OnEventDetailsClickListener : OnFavoriteClickListener, SpeakerView.OnSpeakerClickListener

    internal interface OnFavoriteClickListener {

        fun onFavoriteClick()
    }
}
