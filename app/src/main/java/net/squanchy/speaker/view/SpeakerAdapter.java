package net.squanchy.speaker.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.speaker.domain.view.Speaker;

class SpeakerAdapter extends RecyclerView.Adapter<SpeakerViewHolder> {

    private List<Speaker> speakers = Collections.emptyList();
    @Nullable
    private SpeakersView.OnSpeakerClickedListener listener;
    private final Context context;

    SpeakerAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return speakers.get(position).numericId();
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_speaker, parent, false);
        return new SpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {
        holder.updateWith(speakers.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }
}
