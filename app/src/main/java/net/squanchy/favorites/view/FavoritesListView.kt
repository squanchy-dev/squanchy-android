package net.squanchy.favorites.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.support.view.CardSpacingItemDecorator

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

        setAdapter(adapter)

        val horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardSpacingItemDecorator(horizontalSpacing, verticalSpacing))
    }

    fun updateWith(newData: Schedule, listener: (Event) -> Unit) {
        adapter.updateWith(newData, listener)
    }
}
