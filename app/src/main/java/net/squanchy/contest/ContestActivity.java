package net.squanchy.contest;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.support.config.DialogLayoutParameters;
import net.squanchy.support.debug.DebugPreferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ContestActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_ACHIEVEMENT_ID = ContestActivity.class.getCanonicalName() + ".achievement_id";
    private static final boolean ANIMATE = true;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private ContestService contestService;

    private TextView contestStatusView;
    private ProgressBar contestProgressView;
    private DebugPreferences debugPreferences;

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

        setContentView(R.layout.activity_contest);

        ContestComponent component = ContestInjector.obtain(this);
        contestService = component.contestService();
        debugPreferences = component.debugPreferences();

        addTestingControlsIfDebugOptionActive((ViewGroup) findViewById(R.id.contest_container));

        contestProgressView = (ProgressBar) findViewById(R.id.contest_progressbar);
        contestStatusView = (TextView) findViewById(R.id.contest_status);

        DialogLayoutParameters.wrapHeight(this)
                .applyTo(getWindow());
    }

    private void addTestingControlsIfDebugOptionActive(ViewGroup container) {
        ContestTester contentTester = new ContestTester(this, debugPreferences);
        if (contentTester.testingEnabled()) {
            contentTester.appendDebugControls(container, contestService);
        }
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
        updateProgressBarWith(standings);

        int missingStands = missingSponsorsCount(standings);
        updateStatusTextWith(missingStands);
    }

    private void updateProgressBarWith(ContestStandings standings) {
        contestProgressView.setVisibility(View.VISIBLE);
        contestProgressView.setMax((int) standings.goal());

        if (isAtLeastNougat()) {
            contestProgressView.setProgress(standings.current(), ANIMATE);
        } else {
            contestProgressView.setProgress(standings.current());
        }
    }

    private boolean isAtLeastNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    private int missingSponsorsCount(ContestStandings standings) {
        return (int) (standings.goal() - standings.current());
    }

    private void updateStatusTextWith(int missingStands) {
        contestStatusView.setVisibility(View.VISIBLE);

        if (missingStands > 0) {
            CharSequence status = getResources()
                    .getQuantityString(R.plurals.contest_status_missing_sponsors, missingStands, missingStands);

            contestStatusView.setText(status);
        } else {
            contestStatusView.setText(R.string.contest_completed);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.clear();
    }
}
