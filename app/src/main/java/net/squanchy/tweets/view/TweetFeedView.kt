package net.squanchy.tweets.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

import net.squanchy.R
import net.squanchy.support.view.CardSpacingItemDecorator

class TweetFeedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun onFinishInflate() {
        super.onFinishInflate()

        layoutManager = LinearLayoutManager(context)

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.card_vertical_margin)
        addItemDecoration(CardSpacingItemDecorator(horizontalSpacing, verticalSpacing))
    }
}
