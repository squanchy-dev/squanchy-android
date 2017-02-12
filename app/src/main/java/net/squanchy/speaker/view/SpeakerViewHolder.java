package net.squanchy.speaker.view;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.speaker.Speaker;

public class SpeakerViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final ImageView speakerImage;
    private final TextView speakerName;

    public SpeakerViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        speakerImage = (ImageView) itemView.findViewById(R.id.speaker_image);
        speakerName = (TextView) itemView.findViewById(R.id.speaker_name);
    }

    public void bindTo(Speaker speaker, @Nullable SpeakersView.OnSpeakerClickedListener listener){
        speakerName.setText(speaker.getCompleteName());
        itemView.setOnClickListener(v -> {
            if (listener != null){
                listener.onSpeakerClicked(speaker.id());
            }
        });
    }
}
