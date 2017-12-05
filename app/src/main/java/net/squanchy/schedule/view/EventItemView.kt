package net.squanchy.schedule.view

import android.content.Context
import android.util.AttributeSet

import net.squanchy.schedule.domain.view.Event
import net.squanchy.support.widget.CardLayout

abstract class EventItemView : CardLayout {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    abstract fun updateWith(event: Event)
}
