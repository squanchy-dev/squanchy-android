package net.squanchy.favorites.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
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

    override fun setButtonState(favorited: Boolean) {
        // Updates the FAB image state to trigger an AVD animation.
        val stateSet = intArrayOf(android.R.attr.state_checked * if (favorited) 1 else -1)
        favoriteFab.setImageState(stateSet, true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun showAchievement(message: String) {
        Snackbar.make(this, message.parseHtml(), Snackbar.LENGTH_LONG).show()
    }

    private val favoriteButtonClickListener = View.OnClickListener {
        presentButtonIcon(counter, this)
        presentAchievementMessage(counter, this, ::initialAchieventMessage, ::perseveranceAchievementMessage)
    }

    private fun initialAchieventMessage() = resources.getString(R.string.favorites_achievement_fast_learner)

    private fun perseveranceAchievementMessage() = resources.getString(R.string.favorites_achievement_persevering)
}
