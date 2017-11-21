package net.squanchy.favorites.view

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import net.squanchy.R

class FavoritesSignedInEmptyLayout @JvmOverloads constructor(
        context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : LinearLayout(
            context, attrs, defStyleAttr, defStyleRes), FavoritesSignedInEmptyLayoutView {

    lateinit var favoriteButton: FloatingActionButton

    private var counter = 0

    init {
        super.setOrientation(VERTICAL)
    }

    override fun setOrientation(orientation: Int): Nothing {
        throw UnsupportedOperationException("Changing orientation is not supported for ${FavoritesSignedInEmptyLayout::class.java.simpleName}")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onFinishInflate() {
        super.onFinishInflate()

        favoriteButton = findViewById(R.id.favorite_fab_example)
        favoriteButton.setOnClickListener(this::favoriteButtonClickListener)
    }

    override fun updateCounter(counter: Int) {
        this.counter = counter
    }

    override fun setButtonImage(resId: Int) = favoriteButton.setImageResource(resId)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun showAchievement(message: String) = Snackbar.make(this, readAsHtml(message), Snackbar.LENGTH_LONG).show()


    private fun favoriteButtonClickListener(view: View) {
        handleFavoriteButtonClick(counter, this, this::favoritesFilledIconId, this::favoritesEmptyIconId,
                this::initialAchieventMessage, this::perseveranceAchievementMessage)
    }

    private fun favoritesFilledIconId() = R.drawable.ic_favorite_filled

    private fun favoritesEmptyIconId() = R.drawable.ic_favorite_empty

    private fun initialAchieventMessage() = resources.getString(R.string.favorites_achievement_fast_learner)

    private fun perseveranceAchievementMessage() = resources.getString(R.string.favorites_achievement_persevering)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun readAsHtml(message: String): CharSequence {
        return Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY)
    }

}
