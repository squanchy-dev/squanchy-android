package net.squanchy.contest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ContestActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_ACHIEVEMENT_ID = ContestActivity.class.getCanonicalName() + ".achievement_id";

    private ContestService contestService;
    private CompositeDisposable subscriptions = new CompositeDisposable();
    private TextView contestResults;

    public static Intent createIntent(Context context, String achievementId) {
        Intent intent = new Intent(context, ContestActivity.class);
        intent.putExtra(EXTRA_ACHIEVEMENT_ID, achievementId);
        return intent;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ContestActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contest_summary);

        contestResults = (TextView) findViewById(R.id.contest_result);

        ContestComponent component = ContestInjector.obtain(this);
        contestService = component.contestService();
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscriptions.add(contestService.standings()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWith));

        Intent intent = getIntent();
        addAchievementFromIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        addAchievementFromIntent(intent);
    }

    private void addAchievementFromIntent(Intent intent) {
        String achievementId = intent.getStringExtra(EXTRA_ACHIEVEMENT_ID);

        if (achievementId != null) {
            contestService.addAchievement(achievementId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
    }

    private void updateWith(ContestStandings standings) {
        contestResults.setText(
                String.format(Locale.US, "Checked %1$d out of %2$d stands: \n%3$s",
                        standings.current(),
                        standings.goal(),
                        getContentMessage(standings.current(), standings.goal())));
    }

    private String getContentMessage(int current, float goal) {
        if (current == goal) {
            return "Congratulation, you won!";
        } else {
            int missingStands = (int) (goal - current);
            return String.format(Locale.US, "Still missing %1$d stands", missingStands);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.dispose();
    }
}
