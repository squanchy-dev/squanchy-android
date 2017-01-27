package com.connfa.schedule.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.connfa.PageView;
import com.connfa.R;
import com.connfa.model.data.Event;

import java.util.List;

public class SchedulePageView extends FrameLayout implements PageView<List<Event>> {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public SchedulePageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SchedulePageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void updateWith(List<Event> data) {
        // TODO bind to adapter
    }
}
