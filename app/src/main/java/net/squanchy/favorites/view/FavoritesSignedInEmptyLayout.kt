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
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable
import net.squanchy.R
import net.squanchy.favorites.favoritesSignedInEmptyLayoutPresenter

class FavoritesSignedInEmptyLayout @JvmOverloads constructor(
        context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : LinearLayout(
            context, attrs, defStyleAttr, defStyleRes) {

    lateinit var favoriteButton: FloatingActionButton

    private var counter = 0

    val disposable = CompositeDisposable()

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

        val clickEvent = clickEventObservable(favoriteButton)

        val viewState = favoritesSignedInEmptyLayoutPresenter(clickEvent)

        disposable.add(viewState.subscribe { displayState ->

            counter = displayState.counter

            if (displayState.filledIcon) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_empty)
            }

            if (displayState.fastLearner) {
                showAchievement(R.string.favorites_achievement_fast_learner)
            }

            if (displayState.perseverant) {
                showAchievement(R.string.favorites_achievement_persevering)
                favoriteButton.setEnabled(false)
            }
        })

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.clear()
    }

    private fun clickEventObservable(favoriteButton: FloatingActionButton): Observable<FavoritesClickEvent> {
        return Observable.create { emitter: ObservableEmitter<FavoritesClickEvent> ->

            favoriteButton.setOnClickListener { view: View? ->
                emitter.onNext(FavoritesClickEvent(counter))
            }

            emitter.setCancellable{ favoriteButton.setOnClickListener(null) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showAchievement(stringResId: Int) {
        Snackbar.make(this, readAsHtml(stringResId), Snackbar.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun readAsHtml(stringResId: Int): CharSequence {
        val text = resources.getString(stringResId)
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }

    internal data class FavoritesClickEvent(val counter: Int)

}
