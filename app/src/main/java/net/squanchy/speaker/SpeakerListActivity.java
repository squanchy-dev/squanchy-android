package net.squanchy.speaker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import net.squanchy.R;
import net.squanchy.speaker.view.SpeakersView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class SpeakerListActivity extends AppCompatActivity implements SpeakersView.OnSpeakerClickedListener {

    private SpeakersView speakerPageView;
    private View progressBar;
    private Disposable subscription;
    private SpeakerService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_list);

        speakerPageView = (SpeakersView) findViewById(R.id.speakersView);
        progressBar = findViewById(R.id.progressBar);

        SpeakerComponent component = SpeakerInjector.obtain(this);
        service = component.service();

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_speaker_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscription = service.speakers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, Timber::e);
    }

    private void onSuccess(List<Speaker> speakers) {
        progressBar.setVisibility(View.GONE);
        speakerPageView.updateWith(speakers, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.dispose();
    }

    @Override
    public void onSpeakerClicked(long speakerId) {
        Toast.makeText(this, "You've clicked speaker with id " + speakerId, Toast.LENGTH_SHORT).show();
    }
}
