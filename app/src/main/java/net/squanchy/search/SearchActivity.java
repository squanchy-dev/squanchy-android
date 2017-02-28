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

import java.util.List;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

import timber.log.Timber;

public class SearchActivity extends TypefaceStyleableActivity {

    private static final int SPEECH_REQUEST_CODE = 1223;
    private EditText searchField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchField = (EditText) findViewById(R.id.search_field);
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.voice_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_voice_search) {
            onSearchClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSearchClicked() {
        Intent voiceSearchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceSearchIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        voiceSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        try {
            startActivityForResult(voiceSearchIntent, SPEECH_REQUEST_CODE);
        }catch (ActivityNotFoundException e){
            Timber.e(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchField.setText(results.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
