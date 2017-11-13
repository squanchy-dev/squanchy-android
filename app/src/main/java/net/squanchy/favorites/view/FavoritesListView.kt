package net.squanchy.favorites.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import net.squanchy.R
import net.squanchy.schedule.domain.view.Schedule
import net.squanchy.schedule.view.ScheduleViewPagerAdapter
import net.squanchy.support.view.CardSpacingItemDecorator

internal class FavoritesListView @JvmOverloads constructor
    (context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    private lateinit var adapter: FavoritesAdapter

    override fun onFinishInflate() {
        super.onFinishInflate()

        val layoutManager = LinearLayoutManager(context)
        setLayoutManager(layoutManager)

        adapter = FavoritesAdapter(context)
        setAdapter(adapter)

        val horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardSpacingItemDecorator(horizontalSpacing, verticalSpacing))
    }

    // TODO #333 convert second parameter to a lambda after converting FavoritesPageView to kotlin
    fun updateWith(newData: Schedule, listener: ScheduleViewPagerAdapter.OnEventClickedListener) {
        adapter.updateWith(newData, listener)
    }
}