package net.squanchy.support.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MenuItem
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.squanchy.support.graphics.CircularRevealDrawable
import net.squanchy.support.view.Hotspot

class InterceptingBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private var lastUpEvent: MotionEvent? = null
    private var listener: BottomNavigationView.OnNavigationItemSelectedListener? = null
    var colorProvider: (() -> Int)? = null

    var revealDurationMillis: Int

    private val bottomNavigationMenuView: BottomNavigationMenuView
        get() = getChildAt(0) as BottomNavigationMenuView

    init {
        super.setOnNavigationItemSelectedListener { onItemSelected(it) }
        revealDurationMillis = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    private fun onItemSelected(menuItem: MenuItem): Boolean {
        var itemSelected = true
        if (listener != null) {
            itemSelected = listener!!.onNavigationItemSelected(menuItem)
        }

        if (itemSelected && colorProvider != null) {
            startCircularReveal(colorProvider!!(), menuItem)
        }

        return itemSelected
    }

    private fun startCircularReveal(@ColorInt color: Int, menuItem: MenuItem) {
        val revealDrawable = background as CircularRevealDrawable
        setBackgroundHotspot(revealDrawable, menuItem)
        revealDrawable.animateToColor(color, revealDurationMillis)
    }

    private fun setBackgroundHotspot(revealDrawable: CircularRevealDrawable, menuItem: MenuItem) {
        val (x, y) = lastUpEvent?.let { Hotspot.fromMotionEvent(it) } ?: getHotspotFor(menuItem)
        revealDrawable.setHotspot(x, y)
    }

    private fun getHotspotFor(menuItem: MenuItem): Hotspot {
        val selectedPosition = positionFor(menuItem)
        val menuView = bottomNavigationMenuView
        val selectedItemView = menuView.getChildAt(selectedPosition)

        return Hotspot.fromCenterOf(selectedItemView)
            .offsetToParent(menuView)
    }

    @IntRange(from = 0)
    private fun positionFor(menuItem: MenuItem): Int {
        val menuSize = menu.size()
        for (i in 0 until menuSize) {
            val itemId = menu.getItem(i).itemId
            if (itemId == menuItem.itemId) {
                return i
            }
        }
        throw IllegalArgumentException("Menu item $menuItem not found in the bottom bar items")
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        val background = background
        if (background is CircularRevealDrawable) {
            updateBackgroundColor(background, color)
        } else {
            setBackgroundColor(color)
        }
    }

    override fun setBackground(background: Drawable?) {
        if (background is ColorDrawable) {
            val currentBackground = getBackground()
            val newColor = background.color
            updateBackgroundColor(currentBackground, newColor)
        } else {
            throw IllegalArgumentException("Only ColorDrawables are supported")
        }
    }

    private fun updateBackgroundColor(drawable: Drawable?, newColor: Int) {
        if (drawable is CircularRevealDrawable) {
            drawable.color = newColor
            invalidate()
        } else {
            super.setBackground(CircularRevealDrawable.from(newColor))
        }
    }

    fun cancelTransitions() {
        val background = background
        (background as? CircularRevealDrawable)?.cancelTransitions()
    }

    override fun setOnNavigationItemSelectedListener(listener: BottomNavigationView.OnNavigationItemSelectedListener?) {
        this.listener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            val motionEvent = MotionEvent.obtainNoHistory(ev)
            motionEvent.setLocation(ev.x, ev.y)
            lastUpEvent = motionEvent
        }
        return super.onInterceptTouchEvent(ev)
    }

    // This is a hacky solution to BottomNavigationView's lack of APIs :(
    @SuppressLint("RestrictedApi")
    fun selectItemAt(@IntRange(from = 0) position: Int) {
        menu.getItem(position).isChecked = true
        bottomNavigationMenuView.updateMenuView()
    }
}
