package net.squanchy.speaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.Navigator;
import net.squanchy.speaker.widget.SpeakerDetailsLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SpeakerDetailsActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_SPEAKER_ID = SpeakerDetailsActivity.class.getCanonicalName() + ".speaker_id";

    private CompositeDisposable subscriptions;
    private SpeakerDetailsService service;
    private Navigator navigator;

    private SpeakerDetailsLayout headerLayout;

    public static Intent createIntent(Context context, String speakerId) {
        Intent intent = new Intent(context, SpeakerDetailsActivity.class);
        intent.putExtra(EXTRA_SPEAKER_ID, speakerId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speaker_details);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        SpeakerDetailsComponent component = SpeakerDetailsInjector.obtain(this);
        service = component.service();
        navigator = component.navigator();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar);

        headerLayout = (SpeakerDetailsLayout) findViewById(R.id.speaker_details_header);

        subscriptions = new CompositeDisposable();
    }

    private void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String speakerId = intent.getStringExtra(EXTRA_SPEAKER_ID);

        subscriptions.add(service.speaker(speakerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speaker -> headerLayout.updateWith(speaker)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.dispose();
    }
}
