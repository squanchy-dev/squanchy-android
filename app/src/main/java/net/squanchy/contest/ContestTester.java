package net.squanchy.contest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import net.squanchy.R;
import net.squanchy.support.debug.DebugPreferences;

import io.reactivex.android.schedulers.AndroidSchedulers;

class ContestTester {

    private static final int MAX_ACHIEVEMENT_ID = Integer.MAX_VALUE;

    private final Activity activity;
    private final DebugPreferences debugPreferences;
    private final Random random;

    ContestTester(Activity activity, DebugPreferences debugPreferences) {
        this.activity = activity;
        this.debugPreferences = debugPreferences;
        this.random = new Random();
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
        contestService.addAchievement(generateRandomAchievementId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private String generateRandomAchievementId() {
        return String.valueOf(random.nextInt(MAX_ACHIEVEMENT_ID));
    }
}
