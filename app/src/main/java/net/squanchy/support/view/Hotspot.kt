package net.squanchy.support.view

import android.view.MotionEvent
import android.view.View

@Suppress("DataClassPrivateConstructor")
data class Hotspot private constructor(val x: Float, val y: Float) {

    fun offsetToParent(parent: View) = Hotspot(x + parent.x, y + parent.y)

    companion object {
        fun fromMotionEvent(event: MotionEvent) = Hotspot(event.x, event.y)

        fun fromCenterOf(view: View) : Hotspot {
            val x = view.x + view.width / 2f
            val y = view.y + view.height / 2f
            return Hotspot(x, y)
        }
    }
}
