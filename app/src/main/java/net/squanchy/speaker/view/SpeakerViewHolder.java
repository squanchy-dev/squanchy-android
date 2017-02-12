package net.squanchy.speaker.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.speaker.Speaker;

public class SpeakerViewHolder extends RecyclerView.ViewHolder {

    private ImageView speakerImage;
    private TextView speakerName;

    public SpeakerViewHolder(View itemView) {
        super(itemView);

        speakerImage = (ImageView) itemView.findViewById(R.id.speaker_image);
        speakerName = (TextView) itemView.findViewById(R.id.speaker_name);
    }

    public void bindTo(Speaker speaker){

        speakerName.setText(speaker.getCompleteName());
    }
}
