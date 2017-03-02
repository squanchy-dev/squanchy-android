package net.squanchy.search.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.search.model.Speaker;

class SpeakerAdapter extends RecyclerView.Adapter<SpeakerViewHolder> {

    private List<Speaker> speakers = Collections.emptyList();
    @Nullable
    private SpeakersView.OnSpeakerClickedListener listener;

    private final ImageLoader imageLoader;
    private final Context context;

    SpeakerAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
        imageLoader = ImageLoaderInjector.obtain(context).imageLoader();
    }

    @Override
    public long getItemId(int position) {
        return speakers.get(position).id();
    }

    public List<Speaker> speakers() {
        return speakers;
    }

    public void updateWith(List<Speaker> speakers, @Nullable SpeakersView.OnSpeakerClickedListener listener) {
        this.speakers = speakers;
        this.listener = listener;
    }

    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new SpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {
        holder.updateWith(speakers.get(position), imageLoader, listener);
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }
}
