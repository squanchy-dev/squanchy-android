package net.squanchy.search.view

import android.content.Context
import android.graphics.Rect
import android.support.annotation.Px
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event
import net.squanchy.search.SearchResult
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.unwrapToActivityContext
import net.squanchy.support.widget.CardLayout

class SearchRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private lateinit var adapter: SearchAdapter

    override fun onFinishInflate() {
        super.onFinishInflate()

        val gridLayoutManager = GridLayoutManager(context, COLUMNS_COUNT)
        layoutManager = gridLayoutManager

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardOnlySpacingItemDecorator(horizontalSpacing, verticalSpacing))

        adapter = SearchAdapter(context.unwrapToActivityContext())
        setAdapter(adapter)
        clipToPadding = false
    }

    fun updateWith(searchResult: SearchResult.Success, listener: OnSearchResultClickListener) {
        if (getAdapter() == null) {
            super.setAdapter(adapter)
        }

        adapter.updateWith(searchResult, listener)

        val layoutManager = layoutManager as GridLayoutManager
        val spanSizeLookup = adapter.createSpanSizeLookup(COLUMNS_COUNT)
        layoutManager.spanSizeLookup = spanSizeLookup
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

    companion object {

        private const val COLUMNS_COUNT = 4
    }
}
