package net.squanchy.contest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.support.debug.DebugPreferences;

import io.reactivex.android.schedulers.AndroidSchedulers;

class ContestTester {

    private final Activity activity;
    private final DebugPreferences debugPreferences;

    ContestTester(Activity activity) {
        this.activity = activity;
        this.debugPreferences = new DebugPreferences(this.activity);
    }

    boolean testingEnabled() {
        return debugPreferences.contestTestingEnabled();
    }

    void appendDebugControls(ViewGroup viewGroup, ContestService contestService) {
        LayoutInflater factory = LayoutInflater.from(activity);
        View controlsContainer = factory.inflate(R.layout.view_contest_tester, viewGroup, false);
        viewGroup.addView(controlsContainer);

        controlsContainer.findViewById(R.id.contest_testing_add_one)
                .setOnClickListener(view -> unlockRandomAchievement(contestService));
    }

    private void unlockRandomAchievement(ContestService contestService) {
        contestService.addAchievement(String.valueOf(Math.random() * Integer.MAX_VALUE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
