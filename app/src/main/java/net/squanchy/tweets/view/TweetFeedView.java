package net.squanchy.tweets.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import net.squanchy.R;
import net.squanchy.support.view.CardSpacingItemDecorator;

public class TweetFeedView extends RecyclerView {

    public TweetFeedView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetFeedView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);

        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin);
        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin);
        addItemDecoration(new CardSpacingItemDecorator(horizontalSpacing, verticalSpacing));
    }
}
