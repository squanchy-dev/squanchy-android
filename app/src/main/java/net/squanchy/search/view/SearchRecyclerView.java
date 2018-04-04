package net.squanchy.search.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.search.SearchResults;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.support.widget.CardLayout;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class SearchRecyclerView extends RecyclerView {

    private SearchAdapter adapter;

    private static final int COLUMNS_COUNT = 4;

    public SearchRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setItemAnimator(null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), COLUMNS_COUNT);
        setLayoutManager(gridLayoutManager);

        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin);
        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin);
        addItemDecoration(new CardOnlySpacingItemDecorator(horizontalSpacing, verticalSpacing));

        adapter = new SearchAdapter(unwrapToActivityContext(getContext()));
        setAdapter(adapter);
        setClipToPadding(false);
    }

    public void updateWith(SearchResults searchResults, OnSearchResultClickListener listener) {
        if (getAdapter() == null) {
            super.setAdapter(adapter);
        }

        adapter.updateWith(searchResults, listener);

        GridLayoutManager layoutManager = (GridLayoutManager) getLayoutManager();
        GridLayoutManager.SpanSizeLookup spanSizeLookup = adapter.createSpanSizeLookup(COLUMNS_COUNT);
        layoutManager.setSpanSizeLookup(spanSizeLookup);
    }

    public interface OnSearchResultClickListener {

        void onEventClicked(Event event);

        void onSpeakerClicked(Speaker speaker);
    }

    private static final class CardOnlySpacingItemDecorator extends ItemDecoration {

        @Px
        private final int horizontalSpacing;
        @Px
        private final int verticalSpacing;

        CardOnlySpacingItemDecorator(@Px int horizontalSpacing, @Px int verticalSpacing) {
            super();

            this.horizontalSpacing = horizontalSpacing;
            this.verticalSpacing = verticalSpacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            if (!(view instanceof CardLayout)) {
                return;
            }

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
