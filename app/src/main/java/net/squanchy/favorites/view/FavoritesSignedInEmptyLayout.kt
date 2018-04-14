package net.squanchy.favorites.view

import android.content.Context
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.merge_no_favorites_view.view.*
import net.squanchy.R
import net.squanchy.support.text.parseHtml

class FavoritesSignedInEmptyLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), FavoritesSignedInEmptyLayoutView {

    private var counter = 0

    override fun onFinishInflate() {
        super.onFinishInflate()

        favoriteFab.setOnClickListener(favoriteButtonClickListener)
    }

    override fun updateCounter(counter: Int) {
        this.counter = counter
    }

    override fun setButtonImage(@DrawableRes resId: Int) = favoriteFab.setImageResource(resId)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun showAchievement(message: String) {
        Snackbar.make(this, message.parseHtml(), Snackbar.LENGTH_LONG).show()
    }

    private val favoriteButtonClickListener = View.OnClickListener {
        presentButtonIcon(counter, this, ::favoritesFilledIconId, ::favoritesEmptyIconId)
        presentAchievementMessage(counter, this, ::initialAchieventMessage, ::perseveranceAchievementMessage)
    }

    private fun favoritesFilledIconId() = R.drawable.ic_favorite_filled

    private fun favoritesEmptyIconId() = R.drawable.ic_favorite_empty

    private fun initialAchieventMessage() = resources.getString(R.string.favorites_achievement_fast_learner)

    private fun perseveranceAchievementMessage() = resources.getString(R.string.favorites_achievement_persevering)
}
