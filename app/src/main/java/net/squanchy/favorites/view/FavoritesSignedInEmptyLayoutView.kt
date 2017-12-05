package net.squanchy.favorites.view

import android.support.annotation.DrawableRes

interface FavoritesSignedInEmptyLayoutView {

    fun updateCounter(counter: Int)

    fun setButtonImage(@DrawableRes resId: Int)

    fun showAchievement(message: String)
}
