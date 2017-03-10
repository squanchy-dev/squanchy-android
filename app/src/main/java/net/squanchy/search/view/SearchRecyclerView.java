package net.squanchy.search.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.Collections;

import net.squanchy.search.SearchResults;
import net.squanchy.speaker.domain.view.Speaker;

public class SearchRecyclerView extends RecyclerView {

    private SpeakerAdapter adapter;

    private static final int COLUMN_NUMBER = 4;

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), COLUMN_NUMBER);
        setLayoutManager(gridLayoutManager);
        adapter = new SpeakerAdapter(getContext());
        setAdapter(adapter);
        setClipToPadding(false);
    }

    public void updateWith(SearchResults newData, OnSearchResultClickListener listener) {
        if (getAdapter() == null) {
            super.setAdapter(adapter);
        }

        // TODO undo this once we support the events too
        adapter.updateWith(SearchResults.create(Collections.emptyList(), newData.speakers()), listener);
        
        GridLayoutManager layoutManager = (GridLayoutManager) getLayoutManager();
        GridLayoutManager.SpanSizeLookup spanSizeLookup = adapter.createSpanSizeLookup(COLUMN_NUMBER);
        layoutManager.setSpanSizeLookup(spanSizeLookup);

    }

    public interface OnSearchResultClickListener {

        void onSpeakerClicked(Speaker speaker);
    }
}
