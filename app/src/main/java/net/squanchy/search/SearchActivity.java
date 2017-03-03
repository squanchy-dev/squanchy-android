package net.squanchy.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.speaker.domain.view.Speaker;
import net.squanchy.search.view.SpeakersView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class SearchActivity extends TypefaceStyleableActivity implements SpeakersView.OnSpeakerClickedListener {

    private static final int SPEECH_REQUEST_CODE = 100;

    private EditText searchField;
    private SearchService searchService;
    private SpeakersView speakersView;
    private Disposable subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchField = (EditText) findViewById(R.id.search_field);
        speakersView = (SpeakersView) findViewById(R.id.speakers_view);
        setupToolbar();

        SearchComponent component = SearchInjector.obtain(this);
        searchService = component.service();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscription = searchService.speakers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, Timber::e);
    }

    private void onSuccess(List<Speaker> speakers) {
        speakersView.updateWith(speakers, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.voice_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_voice_search) {
            onVoiceSearchClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onVoiceSearchClicked() {
        Intent voiceSearchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceSearchIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        voiceSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        try {
            startActivityForResult(voiceSearchIntent, SPEECH_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Timber.e(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchField.setText(results.get(0));
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSpeakerClicked(long speakerId) {
        //TODO open the speaker detail view here
        Toast.makeText(this, "Speaker clicked " + speakerId, Toast.LENGTH_SHORT).show();
    }
}
