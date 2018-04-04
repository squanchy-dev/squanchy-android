package net.squanchy.search.view;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import net.squanchy.schedule.view.EventItemView;
import net.squanchy.schedule.view.EventViewHolder;
import net.squanchy.search.SearchResult;

import static net.squanchy.imageloader.ImageLoaderComponentKt.imageLoaderComponent;

class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ImageLoader imageLoader;
    private final Activity activity;

    @Nullable
    private SearchRecyclerView.OnSearchResultClickListener listener;

    private SearchResult.Success searchResult = new SearchResult.Success(Collections.emptyList(), Collections.emptyList());
    private ItemsAdapter itemsAdapter = new ItemsAdapter(searchResult);

    @IntDef({ViewTypeId.HEADER, ViewTypeId.SPEAKER, ViewTypeId.EVENT})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewTypeId {

        int HEADER = 1;
        int SPEAKER = 2;
        int EVENT = 3;
    }

    SearchAdapter(AppCompatActivity activity) {
        super();
        
        this.activity = activity;

        imageLoader = imageLoaderComponent(activity).imageLoader();
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
            return new HeaderViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_search_header, parent, false));
        } else if (viewType == ViewTypeId.SPEAKER) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_search_result_small, parent, false);
            return new SpeakerViewHolder(view);
        } else if (viewType == ViewTypeId.EVENT) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_schedule_event_talk, parent, false);
            return new EventViewHolder((EventItemView) view);
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
            ((HeaderViewHolder) holder).updateWith(itemsAdapter.headerTypeAtAbsolutePosition(position));
        } else if (viewType == ViewTypeId.EVENT) {
            ((EventViewHolder) holder).updateWith(
                    itemsAdapter.eventAtAbsolutePosition(position),
                    event1 -> {
                        if (listener != null) {
                            listener.onEventClicked(event1);
                        }
                        return null;
                    }
            );
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

    public void updateWith(SearchResult.Success searchResult, SearchRecyclerView.OnSearchResultClickListener listener) {
        this.searchResult = searchResult;
        this.itemsAdapter = new ItemsAdapter(searchResult);
        this.listener = listener;

        notifyDataSetChanged();
    }
}
