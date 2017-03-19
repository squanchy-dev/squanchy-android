package net.squanchy.speaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.Navigator;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.speaker.widget.SpeakerDetailsLayout;
import net.squanchy.support.lang.Optional;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SpeakerDetailsActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_SPEAKER_ID = SpeakerDetailsActivity.class.getCanonicalName() + ".speaker_id";

    private CompositeDisposable subscriptions;
    private SpeakerDetailsService service;
    private Navigator navigator;

    private SpeakerDetailsLayout speakerDetailsLayout;

    private Optional<Speaker> speaker = Optional.absent();

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

        speakerDetailsLayout = (SpeakerDetailsLayout) findViewById(R.id.speaker_details_root);

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
                .subscribe(this::onSpeakerRetrieved));
    }

    private void onSpeakerRetrieved(Speaker speaker) {
        speakerDetailsLayout.updateWith(speaker);

        this.speaker = Optional.of(speaker);
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.speaker_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem twitterItem = menu.findItem(R.id.action_speaker_twitter);
        MenuItem websiteItem = menu.findItem(R.id.action_speaker_website);

        if (speaker.isPresent()) {
            Speaker speaker = this.speaker.get();

            twitterItem.setVisible(speaker.twitterUsername().isPresent());
            websiteItem.setVisible(speaker.personalUrl().isPresent());
        } else {
            twitterItem.setVisible(false);
            websiteItem.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_speaker_twitter) {
            navigate().toTwitterProfile(speaker.get().twitterUsername().get());
            return true;
        } else if (itemId == R.id.action_speaker_website) {
            navigate().toExternalUrl(speaker.get().personalUrl().get());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private Navigator navigate() {
        return navigator;
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.dispose();
    }
}
