package net.squanchy.search.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.search.SearchResults;

class SpeakerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @IntDef({ViewTypeId.HEADER, ViewTypeId.SPEAKER, ViewTypeId.TRACK, ViewTypeId.EVENT})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewTypeId {

        int HEADER = 0;
        int SPEAKER = 1;
        int TRACK = 2;
        int EVENT = 3;
    }

    private final ImageLoader imageLoader;
    private final Context context;

    @Nullable
    private SearchRecyclerView.OnSearchResultClickListener listener;

    private SearchResults searchResults = SearchResults.create(Collections.emptyList(), Collections.emptyList());
    private ItemsAdapter itemsAdapter = new ItemsAdapter(searchResults);

    SpeakerAdapter(Context context) {
        this.context = context;

        imageLoader = ImageLoaderInjector.obtain(context).imageLoader();
        setHasStableIds(true);
    }

    @Override
    @ViewTypeId
    public int getItemViewType(int position) {
        return itemsAdapter.viewTypeAtAbsolutePosition(position);
    }

    @Override
    public long getItemId(int position) {
        return itemsAdapter.itemIdAtAbsolutePosition(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ViewTypeId int viewType) {
        if (viewType == ViewTypeId.HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_header, parent, false));
        } else if (viewType == ViewTypeId.SPEAKER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_search_result_small, parent, false);
            return new SpeakerViewHolder(view);
        } else {
            throw new IllegalArgumentException("Item type " + viewType + " not supported");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = itemsAdapter.viewTypeAtAbsolutePosition(position);

        if (viewType == ViewTypeId.SPEAKER) {
            ((SpeakerViewHolder) holder).updateWith(itemsAdapter.speakerAtAbsolutePosition(position), imageLoader, listener);
        } else if (viewType == ViewTypeId.HEADER) {
            ((HeaderViewHolder) holder).updateWith(itemsAdapter.headerTextAtAbsolutePosition(position));
        } else {
            throw new IllegalArgumentException("Item type " + viewType + " not supported");
        }
    }

    GridLayoutManager.SpanSizeLookup createSpanSizeLookup(int columnsCount) {
        return new GridSpanSizeLookup(itemsAdapter, columnsCount);
    }

    @Override
    public int getItemCount() {
        return itemsAdapter.totalItemsCount();
    }

    public void updateWith(SearchResults searchResults, @Nullable SearchRecyclerView.OnSearchResultClickListener listener) {
        this.searchResults = searchResults;
        this.itemsAdapter = new ItemsAdapter(searchResults);
        this.listener = listener;

        notifyDataSetChanged();
    }
}
