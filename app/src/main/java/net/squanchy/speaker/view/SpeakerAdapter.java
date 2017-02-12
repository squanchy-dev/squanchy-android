package net.squanchy.speaker.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.speaker.Speaker;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerViewHolder> {

    private List<Speaker> speakerList = Collections.emptyList();
    private SpeakersView.OnSpeakerClickedListener listener;

    public SpeakerAdapter() {
    }

    public void updateWith(List<Speaker> speakerList, SpeakersView.OnSpeakerClickedListener listener) {
        this.speakerList = speakerList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speaker, parent, false);
        return new SpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {
        holder.bindTo(speakerList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return speakerList.size();
    }
}
