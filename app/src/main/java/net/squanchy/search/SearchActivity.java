package net.squanchy.search;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.Navigator;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.search.view.SearchRecyclerView;
import net.squanchy.speaker.domain.view.Speaker;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

@SuppressWarnings("PMD.ExcessiveImports")           // Might use some refactoring later on to extract some collaborator
public class SearchActivity extends TypefaceStyleableActivity implements SearchRecyclerView.OnSearchResultClickListener {

    private static final int SPEECH_REQUEST_CODE = 100;
    private static final int QUERY_DEBOUNCE_TIMEOUT = 250;
    private static final int DELAY_ENOUGH_FOR_FOCUS_TO_HAPPEN_MILLIS = 50;
    private static final int MIN_QUERY_LENGTH = 2;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private Navigator navigator;

    private SearchTextWatcher searchTextWatcher;

    private EditText searchField;
    private SearchService searchService;
    private SearchRecyclerView searchRecyclerView;

    private boolean hasQuery;
    private View emptyView;
    private TextView emptyViewMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchField = findViewById(R.id.search_field);
        emptyView = findViewById(R.id.empty_view);
        emptyViewMessage = findViewById(R.id.empty_view_message);

        searchRecyclerView = findViewById(R.id.speakers_view);
        setupToolbar();

        SearchComponent component = SearchInjector.obtain(this);
        searchService = component.service();
        navigator = component.navigator();
    }

    @SuppressWarnings("ConstantConditions") // We set up the ActionBar ourselves, so it's not null
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        PublishSubject<String> querySubject = PublishSubject.create();
        searchTextWatcher = new SearchTextWatcher(querySubject);
        searchField.addTextChangedListener(searchTextWatcher);

        Disposable speakersSubscription = searchService.speakers()
                .map(speakers -> SearchResults.Companion.create(Collections.emptyList(), speakers))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResults -> searchRecyclerView.updateWith(searchResults, this), Timber::e);

        Disposable searchSubscription = querySubject.throttleLast(QUERY_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .doOnNext(this::updateSearchActionIcon)
                .flatMap(searchService::find)
                .doOnNext(searchResults -> speakersSubscription.dispose())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReceivedSearchResults, Timber::e);

        subscriptions.add(speakersSubscription);
        subscriptions.add(searchSubscription);

        searchField.requestFocus();
        searchField.postDelayed(() -> requestShowKeyboard(searchField), DELAY_ENOUGH_FOR_FOCUS_TO_HAPPEN_MILLIS);
    }

    private void requestShowKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
            }
        });
    }

    private void updateSearchActionIcon(String query) {
        hasQuery = !TextUtils.isEmpty(query);
        supportInvalidateOptionsMenu();
    }

    private void onReceivedSearchResults(SearchResults searchResults) {
        if (searchResults.isEmpty()) {
            searchRecyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);

            CharSequence query = searchField.getText();
            updateEmptyStateMessageFor(query);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            searchRecyclerView.setVisibility(View.VISIBLE);

            searchRecyclerView.updateWith(searchResults, this);
        }
    }

    private void updateEmptyStateMessageFor(CharSequence query) {
        if (query == null || query.length() < MIN_QUERY_LENGTH) {
            emptyViewMessage.setText(R.string.start_typing_to_search);
        } else {
            emptyViewMessage.setText(R.string.no_results);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        subscriptions.clear();

        if (searchTextWatcher != null) {
            searchField.removeTextChangedListener(searchTextWatcher);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem voiceSearchItem = menu.findItem(R.id.action_voice_search);
        voiceSearchItem.setVisible(!hasQuery);
        MenuItem clearQueryItem = menu.findItem(R.id.action_clear_query);
        clearQueryItem.setVisible(hasQuery);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_voice_search) {
            onVoiceSearchClicked();
            return true;
        } else if (item.getItemId() == R.id.action_clear_query) {
            clearSearchQuery();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(0, android.R.anim.fade_out);
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

    private void clearSearchQuery() {
        searchField.setText("");
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
        navigate().toSpeakerDetails(speaker.getId());
    }

    @Override
    public void onEventClicked(Event event) {
        navigate().toEventDetails(event.getId());
    }

    private Navigator navigate() {
        return navigator;
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
            if (query == null) {
                return;
            }
            querySubject.onNext(query.toString());
        }

        @Override
        public void afterTextChanged(Editable query) {
            // No-op
        }
    }
}
