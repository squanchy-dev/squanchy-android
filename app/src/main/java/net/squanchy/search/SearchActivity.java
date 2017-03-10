package net.squanchy.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.search.view.SearchRecyclerView;
import net.squanchy.speaker.domain.view.Speaker;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class SearchActivity extends TypefaceStyleableActivity implements SearchRecyclerView.OnSearchResultClickListener {

    private static final int SPEECH_REQUEST_CODE = 100;
    private static final int QUERY_DEBOUNCE_TIMEOUT = 250;

    private final CompositeDisposable subscriptions = new CompositeDisposable();
    private final PublishSubject<String> querySubject = PublishSubject.create();

    private EditText searchField;
    private SearchService searchService;
    private SearchRecyclerView searchRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchField = (EditText) findViewById(R.id.search_field);
        searchField.addTextChangedListener(new SearchTextWatcher(querySubject));

        searchRecyclerView = (SearchRecyclerView) findViewById(R.id.speakers_view);
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

        Disposable speakersSubscription = searchService.speakers()
                .map(speakers -> SearchResults.create(Collections.emptyList(), speakers))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResults -> searchRecyclerView.updateWith(searchResults, this), Timber::e);

        Disposable searchSubscription = querySubject.debounce(QUERY_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .flatMap(s -> searchService.find(s))
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResults -> searchRecyclerView.updateWith(searchResults, this), Timber::e);

        subscriptions.add(speakersSubscription);
        subscriptions.add(searchSubscription);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.dispose();
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
        } else if (item.getItemId() == android.R.id.home) {
            finish();
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
    public void onSpeakerClicked(Speaker speaker) {
        // TODO open the speaker detail view here
        Toast.makeText(this, "Speaker clicked " + speaker, Toast.LENGTH_SHORT).show();
    }

    private static class SearchTextWatcher implements TextWatcher {

        private final PublishSubject<String> querySubject;

        SearchTextWatcher(PublishSubject<String> querySubject) {
            this.querySubject = querySubject;
        }

        @Override
        public void beforeTextChanged(CharSequence query, int start, int count, int after) {
            // No-op
        }

        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {
            querySubject.onNext(query.toString());
        }

        @Override
        public void afterTextChanged(Editable query) {
            // No-op
        }
    }
}
