package net.squanchy.favorites.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;
import net.squanchy.support.view.CardSpacingItemDecorator;

public class FavoritesListView extends RecyclerView {

    private FavoritesAdapter adapter;

    public FavoritesListView(Context context) {
        super(context);
    }

    public FavoritesListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoritesListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);
        adapter = new FavoritesAdapter(getContext());
        setAdapter(adapter);

        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin);
        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin);
        addItemDecoration(new CardSpacingItemDecorator(horizontalSpacing, verticalSpacing));
    }

    public void updateWith(Schedule newData, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        adapter.updateWith(newData, listener);
    }
}
