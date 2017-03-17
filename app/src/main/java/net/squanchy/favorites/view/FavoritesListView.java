package net.squanchy.favorites.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;

public class FavoritesListView extends RecyclerView {

    private EventsWithHeadersAdapter adapter;

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
        adapter = new EventsWithHeadersAdapter(getContext());
        setAdapter(adapter);

        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin);
        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin);
        addItemDecoration(new CardSpacingItemDecorator(horizontalSpacing, verticalSpacing));
    }

    public void updateWith(Schedule newData, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        adapter.updateWith(newData, listener);
    }

    private static class CardSpacingItemDecorator extends ItemDecoration {

        @Px
        private final int horizontalSpacing;
        @Px
        private final int verticalSpacing;

        private CardSpacingItemDecorator(@Px int horizontalSpacing, @Px int verticalSpacing) {
            this.horizontalSpacing = horizontalSpacing;
            this.verticalSpacing = verticalSpacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            int position = parent.getChildAdapterPosition(view);
            int count = state.getItemCount();

            int topSpacing = verticalSpacing / 2;
            int bottomSpacing = topSpacing;
            if (position == 0) {
                topSpacing = verticalSpacing;
            } else if (position == count - 1) {
                bottomSpacing = verticalSpacing;
            }

            outRect.set(horizontalSpacing, topSpacing, horizontalSpacing, bottomSpacing);
        }
    }
}
