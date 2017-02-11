package net.squanchy.speaker.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.speaker.Speaker;

import java.util.List;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerViewHolder> {

    private List<Speaker> speakerList;

    public SpeakerAdapter(List<Speaker> speakerList) {
        this.speakerList = speakerList;
    }

    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speaker, parent, false);
        return new SpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {
        holder.bindTo(speakerList.get(position));
    }

    @Override
    public int getItemCount() {
        return speakerList.size();
    }
}
