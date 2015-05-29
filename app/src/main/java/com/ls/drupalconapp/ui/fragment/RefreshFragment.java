package com.ls.drupalconapp.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.ls.drupalconapp.R;

public class RefreshFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPullToRefresh();
    }

    @Override
    public void onRefresh() {
        emulateRefreshing();
    }

    private void initPullToRefresh() {
        View view = getView();
        if(view == null) {
            return;
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        if(mSwipeRefreshLayout == null) {
            throw new IllegalArgumentException("This fragment must contain layout with SwipeRefreshLayout which id equals to R.id.swipeRefreshLayout");
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    protected void onRefreshClicked() {
        if(mSwipeRefreshLayout.isRefreshing()) {
            return;
        }

        mSwipeRefreshLayout.setRefreshing(true);

        emulateRefreshing();
    }

    private void emulateRefreshing() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = getActivity();
                if(activity == null || activity.isFinishing()) {
                    return;
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);

                        Toast.makeText(getActivity(), R.string.data_refreshed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 5000);
    }
}
