package net.squanchy.search.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.search.SearchResult
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.unwrapToActivityContext
import net.squanchy.support.view.setAdapterIfNone
import net.squanchy.support.widget.CardLayout

class SearchRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private val columnsCount = context.resources.getInteger(R.integer.search_columns_count)

    private lateinit var adapter: SearchAdapter

    override fun onFinishInflate() {
        super.onFinishInflate()

        val gridLayoutManager = GridLayoutManager(context, columnsCount)
        layoutManager = gridLayoutManager

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardOnlySpacingItemDecorator(horizontalSpacing, verticalSpacing))

        adapter = SearchAdapter(context.unwrapToActivityContext())
        clipToPadding = false
    }

    fun updateWith(searchResult: SearchResult.Success, listener: OnSearchResultClickListener) {
        setAdapterIfNone(adapter)

        adapter.updateWith(searchResult.elements, listener)

        val spanSizeLookup = adapter.createSpanSizeLookup(columnsCount)
        (layoutManager as GridLayoutManager).spanSizeLookup = spanSizeLookup
    }

    interface OnSearchResultClickListener {

        fun onEventClicked(event: Event)

        fun onSpeakerClicked(speaker: Speaker)
    }

    private class CardOnlySpacingItemDecorator(
        @param:Px private val horizontalSpacing: Int,
        @param:Px private val verticalSpacing: Int
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (view !is CardLayout) {
                return
            }

            val position = parent.getChildAdapterPosition(view)
            val count = state.itemCount

            val topSpacing = if (position == 0) verticalSpacing else verticalSpacing / 2
            val bottomSpacing = if (position == count - 1) verticalSpacing else verticalSpacing / 2

            outRect.set(horizontalSpacing, topSpacing, horizontalSpacing, bottomSpacing)
        }
    }
}
