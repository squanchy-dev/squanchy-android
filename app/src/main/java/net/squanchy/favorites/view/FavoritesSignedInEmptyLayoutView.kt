package net.squanchy.favorites.view

interface FavoritesSignedInEmptyLayoutView {

    fun updateCounter(counter: Int)

    fun setButtonImage(resId: Int)

    fun showAchievement(message: String)
}