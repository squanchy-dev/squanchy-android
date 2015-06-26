package com.ls.drupalconapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.data.EventDetailsEvent;
import com.ls.drupalconapp.model.managers.EventManager;
import com.ls.drupalconapp.ui.view.NotifyingScrollView;

public class EventDetailsActivity2 extends ActionBarActivity {

    private static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    private static final String EXTRA_DAY = "EXTRA_DAY";

    private TextView mToolbarTitle;

    private NotifyingScrollView mScrollView;
    private ProgressBar mProgressBar;
    private long mEventId;

    public static void startThisActivity(Activity activity, long eventId, long day) {
        Intent intent = new Intent(activity, EventDetailsActivity2.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        intent.putExtra(EXTRA_DAY, day);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_event_details2);
        mEventId = getIntent().getLongExtra(EXTRA_EVENT_ID, -1);

        initViews();
    }

    private void initViews() {
        initStatusBar();
        initToolbar();
        initView();
        loadEvent();
    }

    private void initStatusBar() {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.viewStatusBarTrans).setVisibility(View.VISIBLE);
        }
    }

    private void initToolbar() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        mToolbarTitle.setAlpha(0f);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mScrollView = (NotifyingScrollView) findViewById(R.id.scrollView);
        mScrollView.setAlpha(0);
        mScrollView.setOnScrollChangedListener(scrollListener);
    }

    private void loadEvent() {
        if (mEventId != -1) {
            new AsyncTask<Void, Void, EventDetailsEvent>() {
                @Override
                protected EventDetailsEvent doInBackground(Void... params) {
                    EventManager manager = Model.instance().getEventManager();
                    return manager.getEventById(mEventId);
                }

                @Override
                protected void onPostExecute(EventDetailsEvent event) {
                    fillEventView(event);
                }
            }.execute();
        }
    }

    private void fillEventView(@NonNull EventDetailsEvent event) {

    }

    private NotifyingScrollView.OnScrollChangedListener scrollListener = new NotifyingScrollView.OnScrollChangedListener() {

        @Override
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            int headerHeight = findViewById(R.id.imgHeader).getHeight();
            float headerRatio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            fadeActionBar(headerRatio);
        }

        private void fadeActionBar(float headerRatio) {
            mToolbarTitle.setAlpha(headerRatio);
        }
    };
}
