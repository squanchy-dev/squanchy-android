package net.squanchy.search.view;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.squanchy.imageloader.ImageLoader;
import net.squanchy.search.SearchItemView;
import net.squanchy.speaker.domain.view.Speaker;

class SpeakerViewHolder extends RecyclerView.ViewHolder {

    SpeakerViewHolder(View itemView) {
        super(itemView);
    }

    public void updateWith(Speaker speaker, ImageLoader imageLoader, @Nullable SpeakersView.OnSpeakerClickedListener listener) {
        ((SearchItemView) itemView).updateWith(speaker, imageLoader, listener);
    }
}
