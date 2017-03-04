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
import java.util.List;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.search.model.TitledList;
import net.squanchy.search.model.SearchListFactory;
import net.squanchy.speaker.domain.view.Speaker;

class SpeakerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @IntDef({ViewTypeId.HEADER, ViewTypeId.SPEAKER /* and more in the future */})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewTypeId {

        int HEADER = 0;
        int SPEAKER = 1;
    }

    private static final int SPEAKER_LIST_OFFSET = 1;

    private final TitledList<Speaker> speakerList = SearchListFactory.buildSpeakerList(null);

    @Nullable
    private SpeakersView.OnSpeakerClickedListener listener;

    private final ItemsAdapter itemsAdapter;
    private final ImageLoader imageLoader;
    private final Context context;

    SpeakerAdapter(Context context) {
        this.context = context;
        itemsAdapter = new ItemsAdapter();
        imageLoader = ImageLoaderInjector.obtain(context).imageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ViewTypeId int viewType) {
        if (viewType == ViewTypeId.HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_header, parent, false));
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_search_generic, parent, false);
            return new SpeakerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (itemsAdapter.getViewTypeAt(position) == ViewTypeId.SPEAKER) {
            int positionInList = position - SPEAKER_LIST_OFFSET;
            ((SpeakerViewHolder) holder).updateWith(speakerList.get(positionInList), imageLoader, listener);
        } else {
            ((HeaderViewHolder) holder).updateWith(speakerList.getTitle());
        }
    }

    @Override
    @ViewTypeId
    public int getItemViewType(int position) {
        return itemsAdapter.getViewTypeAt(position);
    }

    GridLayoutManager.SpanSizeLookup createSpanSizeLookup(int columnsCount) {
        return new GridSpanSizeLookup(itemsAdapter, columnsCount);
    }

    @Override
    public int getItemCount() {
        return itemsAdapter.getTotalItemsCount();
    }

    public List<Speaker> speakers() {
        return speakerList.getItems();
    }

    public void updateWith(List<Speaker> speakers, @Nullable SpeakersView.OnSpeakerClickedListener listener) {
        speakerList.setItems(speakers);
        itemsAdapter.addItems(speakerList);
        this.listener = listener;
    }
}
