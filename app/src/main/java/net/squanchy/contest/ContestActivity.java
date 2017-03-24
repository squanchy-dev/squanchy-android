package net.squanchy.contest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.speaker.SpeakerDetailsActivity;

public class ContestActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_CHECKPOINT_ID = ContestActivity.class.getCanonicalName() + ".checkpoint_id";

    private ContestService contestService;
    TextView result;

    public static Intent createIntent(Context context, String checkpoint) {
        Intent intent = new Intent(context, SpeakerDetailsActivity.class);
        intent.putExtra(EXTRA_CHECKPOINT_ID, checkpoint);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contest_summary);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        result = (TextView) findViewById(R.id.result);

        ContestComponent component = ContestInjector.obtain(this);
        contestService = component.contestService();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String checkpointId = intent.getStringExtra(EXTRA_CHECKPOINT_ID);

        contestService.updateContest(checkpointId)
                .subscribe(this::updateWith);
    }

    private void updateWith(ContestStandings standings) {
        result.setText("got results");
    }
}
