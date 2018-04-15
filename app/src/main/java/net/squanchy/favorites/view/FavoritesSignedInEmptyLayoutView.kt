package net.squanchy.favorites.view

interface FavoritesSignedInEmptyLayoutView {

    fun updateCounter(counter: Int)

    fun setButtonState(favorited: Boolean)

    fun showAchievement(message: String)
}
