package net.squanchy.favorites.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.squanchy.R
import net.squanchy.support.view.CardSpacingItemDecorator
import net.squanchy.support.view.setAdapterIfNone

internal class FavoritesListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private val adapter = FavoritesAdapter(context)

    override fun onFinishInflate() {
        super.onFinishInflate()

        val layoutManager = LinearLayoutManager(context)
        setLayoutManager(layoutManager)

        val horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardSpacingItemDecorator(horizontalSpacing, verticalSpacing))
    }

    fun updateWith(newData: List<FavoritesItem>, showRoom: Boolean, listener: OnFavoriteClickListener) {
        setAdapterIfNone(adapter)
        adapter.updateWith(newData, showRoom, listener)
    }
}
