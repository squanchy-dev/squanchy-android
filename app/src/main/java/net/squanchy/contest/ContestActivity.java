package net.squanchy.contest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ContestActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_CHECKPOINT_ID = ContestActivity.class.getCanonicalName() + ".checkpoint_id";

    private ContestService contestService;
    private TextView contestResults;

    public static Intent createIntent(Context context, String checkpoint) {
        Intent intent = new Intent(context, ContestActivity.class);
        intent.putExtra(EXTRA_CHECKPOINT_ID, checkpoint);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contest_summary);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        contestResults = (TextView) findViewById(R.id.contest_result);

        ContestComponent component = ContestInjector.obtain(this);
        contestService = component.contestService();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String checkpointId = intent.getStringExtra(EXTRA_CHECKPOINT_ID);

        contestService.addAchievement(checkpointId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWith);
    }

    private void updateWith(ContestStandings standings) {
        contestResults.setText(
                String.format(Locale.US, "Checked %1$d out of %2$d stands: \n%3$s",
                        standings.current(),
                        standings.goal(),
                        getContentMessage(standings.current(), standings.goal())));
    }

    private String getContentMessage(int current, int goal) {
        return current == goal ? "Congratulation, you won!" : "Still missing " + (goal - current) + " stands";
    }
}
