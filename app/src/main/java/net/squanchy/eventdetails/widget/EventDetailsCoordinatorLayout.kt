package net.squanchy.eventdetails.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_event_details.view.*
import net.squanchy.R
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

        if (canBeFavorited(event)) {
            favoriteFab.setImageResource(
                    if (event.favorited)
                        R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_empty
            )
            favoriteFab.setOnClickListener { listener.onFavoriteClick() }
            favoriteFab.visibility = View.VISIBLE
        } else {
            favoriteFab.visibility = View.GONE
        }
    }

    private fun canBeFavorited(event: Event) = event.type === Type.TALK || event.type === Type.KEYNOTE

    internal interface OnEventDetailsClickListener : OnFavoriteClickListener, SpeakerView.OnSpeakerClickListener

    internal interface OnFavoriteClickListener {

        fun onFavoriteClick()
    }
}
