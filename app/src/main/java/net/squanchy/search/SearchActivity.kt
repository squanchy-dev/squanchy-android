package net.squanchy.search

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.speech.RecognizerIntent
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.content.systemService
import androidx.view.isInvisible
import androidx.view.isVisible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import net.squanchy.R
import net.squanchy.navigation.Navigator
import net.squanchy.schedule.domain.view.Event
import net.squanchy.search.view.SearchRecyclerView
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.view.enableLightNavigationBar
import timber.log.Timber
import java.util.concurrent.TimeUnit

// Might use some refactoring later on to extract some collaborator
class SearchActivity : AppCompatActivity(), SearchRecyclerView.OnSearchResultClickListener {

    private val subscriptions = CompositeDisposable()

    private lateinit var navigator: Navigator
    private lateinit var searchService: SearchService

    private lateinit var searchTextWatcher: SearchTextWatcher

    private var hasQuery: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        enableLightNavigationBar(this)
        setupToolbar()

        with(searchComponent(this)) {
            searchService = service()
            navigator = navigator()
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        val querySubject = PublishSubject.create<String>()
        searchTextWatcher = SearchTextWatcher(querySubject)
        searchField.addTextChangedListener(searchTextWatcher)

        val searchSubscription = querySubject.throttleLast(QUERY_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .doOnNext(::updateSearchActionIcon)
            .startWith(EMPTY_QUERY)
            .flatMap(searchService::find)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onReceiveSearchResults, Timber::e)

        subscriptions.add(searchSubscription)

        searchField.requestFocus()
        searchField.postDelayed({ requestShowKeyboard(searchField) }, DELAY_ENOUGH_FOR_FOCUS_TO_HAPPEN_MILLIS)
    }

    private fun updateSearchActionIcon(query: String) {
        hasQuery = query.isNotEmpty()
        supportInvalidateOptionsMenu()
    }

    private fun onReceiveSearchResults(searchResult: SearchResult) {
        when (searchResult) {
            is SearchResult.Success -> onSearchSuccessful(searchResult)
            is SearchResult.Error -> onSearchError()
        }
    }

    private fun onSearchSuccessful(searchResult: SearchResult.Success) {
        if (searchResult.isEmpty) {
            emptyViewMessage.loadCompoundDrawableTop(R.drawable.ic_error_outline)

            searchRecyclerView.isInvisible = true
            emptyView.isVisible = true

            val query = searchField.text
            updateEmptyStateMessageFor(query)
        } else {
            emptyView.isInvisible = true
            searchRecyclerView.isVisible = true

            searchRecyclerView.updateWith(searchResult, this)
        }
    }

    private fun onSearchError() {
        emptyViewMessage.loadCompoundDrawableTop(R.drawable.ic_cloud_off)
        emptyViewMessage.setText(R.string.search_error_message)
        searchRecyclerView.isInvisible = true
        emptyView.isVisible = true
    }

    private fun updateEmptyStateMessageFor(query: CharSequence?) {
        if (query?.length ?: 0 < MIN_QUERY_LENGTH) {
            emptyViewMessage.setText(R.string.start_typing_to_search)
        } else {
            emptyViewMessage.setText(R.string.no_results)
        }
    }

    private fun requestShowKeyboard(view: View) {
        val imeManager: InputMethodManager = systemService()
        imeManager.showSoftInput(view, 0, ResultReceiver(Handler()))
    }

    private fun TextView.loadCompoundDrawableTop(@DrawableRes drawableRes: Int) {
        val drawable = ResourcesCompat.getDrawable(resources, drawableRes, theme)
        setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }

    override fun onStop() {
        super.onStop()

        subscriptions.clear()

        searchField.removeTextChangedListener(searchTextWatcher)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val voiceSearchItem = menu.findItem(R.id.action_voice_search)
        voiceSearchItem.isVisible = !hasQuery
        val clearQueryItem = menu.findItem(R.id.action_clear_query)
        clearQueryItem.isVisible = hasQuery
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.action_voice_search -> {
                onVoiceSearchClicked()
                return true
            }
            item.itemId == R.id.action_clear_query -> {
                clearSearchQuery()
                return true
            }
            item.itemId == android.R.id.home -> {
                finish()
                overridePendingTransition(0, android.R.anim.fade_out)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun onVoiceSearchClicked() {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            try {
                startActivityForResult(this, SPEECH_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                Timber.e(e)
            }
        }
    }

    private fun clearSearchQuery() {
        searchField.setText("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            searchField.setText(results[0])
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSpeakerClicked(speaker: Speaker) {
        navigator.toSpeakerDetails(speaker.id)
    }

    override fun onEventClicked(event: Event) {
        navigator.toEventDetails(event.id)
    }

    private class SearchTextWatcher(private val querySubject: PublishSubject<String>) : TextWatcher {

        override fun beforeTextChanged(query: CharSequence, start: Int, count: Int, after: Int) {
            // No-op
        }

        override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
            query?.let { querySubject.onNext(it.toString()) }
        }

        override fun afterTextChanged(query: Editable) {
            // No-op
        }
    }

    companion object {

        private const val SPEECH_REQUEST_CODE = 100
        private const val QUERY_DEBOUNCE_TIMEOUT = 250L
        private const val DELAY_ENOUGH_FOR_FOCUS_TO_HAPPEN_MILLIS = 50L
        private const val MIN_QUERY_LENGTH = 2
        private const val EMPTY_QUERY = ""
    }
}
