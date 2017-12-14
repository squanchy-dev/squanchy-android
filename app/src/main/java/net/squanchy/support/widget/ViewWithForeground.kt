package net.squanchy.support.widget

import android.graphics.drawable.Drawable

interface ViewWithForeground {

    fun foregroundGravity(): Int

    fun setForegroundGravity(foregroundGravity: Int)

    fun setForeground(drawable: Drawable)

    fun foreground(): Drawable
}
