package net.squanchy.speaker.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import net.squanchy.R;
import net.squanchy.speaker.Speaker;

public class SpeakerPageView extends FrameLayout {

    private SpeakerAdapter adapter;

    private RecyclerView speakerList;
    private ProgressBar progressBar;

    public SpeakerPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeakerPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        speakerList = (RecyclerView) findViewById(R.id.speakersList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        speakerList.setLayoutManager(layoutManager);
        adapter = new SpeakerAdapter();
        speakerList.setAdapter(adapter);
    }

    public void updateWith(List<Speaker> newData, OnSpeakerClickedListener listener) {
        adapter.setSpeakerList(newData);
        speakerList.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
    }

    interface OnSpeakerClickedListener {

        void onSpeakerClicked(long speakerId);
    }
}
