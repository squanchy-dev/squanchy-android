package net.squanchy.speaker.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import net.squanchy.speaker.Speaker;

public class SpeakersView extends RecyclerView {

    private SpeakerAdapter adapter;

    public SpeakersView(Context context) {
        super(context);
    }

    public SpeakersView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(layoutManager);
        adapter = new SpeakerAdapter();
        setAdapter(adapter);
        setClipToPadding(false);
    }

    public void updateWith(List<Speaker> newData, OnSpeakerClickedListener listener) {
        adapter.updateWith(newData, listener);
    }

    public interface OnSpeakerClickedListener {

        void onSpeakerClicked(long speakerId);
    }
}
