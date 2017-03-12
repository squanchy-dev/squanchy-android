package net.squanchy.search.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import net.squanchy.schedule.view.ScheduleViewPagerAdapter;
import net.squanchy.search.SearchResults;
import net.squanchy.speaker.domain.view.Speaker;

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
        adapter = new SearchAdapter(getContext());
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

    public interface OnSearchResultClickListener extends ScheduleViewPagerAdapter.OnEventClickedListener {

        void onSpeakerClicked(Speaker speaker);
    }
}
