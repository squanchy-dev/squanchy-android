package net.squanchy.speaker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.squanchy.R;
import net.squanchy.speaker.view.SpeakerPageView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SpeakerListActivity extends AppCompatActivity {

    private SpeakerPageView speakerPageView;
    private Disposable subscription;
    private SpeakerService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_list);

        speakerPageView = (SpeakerPageView) findViewById(R.id.speakersView);

        SpeakerComponent component = SpeakerInjector.obtain(this);
        service = component.service();
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscription = service.speakers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speakerList -> speakerPageView.updateWith(speakerList, null));
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.dispose();
    }
}
