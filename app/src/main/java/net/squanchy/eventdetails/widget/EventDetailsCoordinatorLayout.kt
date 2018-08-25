package net.squanchy.eventdetails.widget

import android.content.Context
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_event_details.view.*
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Event.Type
import net.squanchy.support.widget.SpeakerView

class EventDetailsCoordinatorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    internal fun updateWith(event: Event, listener: OnEventDetailsClickListener) {
        eventDetailsHeaderLayout.updateWith(event, listener)
        eventDetailsLayout.updateWith(event)

        if (event.canBeFavorited) {
            // Updates the FAB image state to trigger an AVD animation.
            val stateSet = intArrayOf(android.R.attr.state_checked * if (event.favorite) 1 else -1)
            favoriteFab.setImageState(stateSet, true)
            favoriteFab.setOnClickListener { listener.onFavoriteClick() }
            favoriteFab.isVisible = true
        } else {
            favoriteFab.isVisible = false
        }
    }

    private val Event.canBeFavorited
        get() = type == Type.TALK || type == Type.KEYNOTE || type == Type.WORKSHOP

    internal interface OnEventDetailsClickListener : OnFavoriteClickListener, SpeakerView.OnSpeakerClickListener

    internal interface OnFavoriteClickListener {

        fun onFavoriteClick()
    }
}
