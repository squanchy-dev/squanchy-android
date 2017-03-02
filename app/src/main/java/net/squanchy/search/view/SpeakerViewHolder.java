package net.squanchy.search.view;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.squanchy.R;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.search.model.Speaker;

class SpeakerViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final ImageView speakerImage;
    private final TextView speakerName;

    SpeakerViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        speakerImage = (ImageView) itemView.findViewById(R.id.speaker_image);
        speakerName = (TextView) itemView.findViewById(R.id.speaker_names);
    }

    public void updateWith(Speaker speaker, ImageLoader imageLoader, @Nullable SpeakersView.OnSpeakerClickedListener listener) {
        speakerName.setText(speaker.fullName());
        loadSpeakerPhoto(speakerImage, speaker.avatarImageURL(), imageLoader);
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSpeakerClicked(speaker.id());
            }
        });
    }

    private void loadSpeakerPhoto(ImageView photoView, String photoUrl, ImageLoader imageLoader) {
        //TODO load photoURL here instead of the hardcoded resource
        StorageReference photoReference = FirebaseStorage.getInstance().getReference("speakers/squanchy.webp");
        imageLoader.load(photoReference).into(photoView);
    }
}
