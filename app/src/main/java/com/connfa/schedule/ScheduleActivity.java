package com.connfa.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.connfa.R;
import com.connfa.navigation.NavigationDrawerActivity;
import com.connfa.navigation.Navigator;

public class ScheduleActivity extends NavigationDrawerActivity {

    @Override
    protected void inflateActivityContent(ViewGroup parent) {
        LayoutInflater.from(this)
                .inflate(R.layout.activity_schedule, parent, true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        // TODO
    }

    @Override
    protected Navigator navigate() {
        return new Navigator() {

            @Override
            public void up() {
                // TODO
            }
        };
    }
}
