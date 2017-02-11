package net.squanchy.speaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.navigation.NavigationDrawerActivity;
import net.squanchy.navigation.Navigator;

public class SpeakerListActivity extends NavigationDrawerActivity {


    @Override
    protected void inflateActivityContent(ViewGroup parent) {
        LayoutInflater.from(this)
                .inflate(R.layout.activity_speaker_list, parent, true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    @Override
    protected Navigator navigate() {
        return null;
    }
}
