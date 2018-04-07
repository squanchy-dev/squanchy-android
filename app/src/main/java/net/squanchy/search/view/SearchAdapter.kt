package net.squanchy.search.view

import android.app.Activity
import android.support.annotation.IntDef
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.imageLoaderComponent
import net.squanchy.schedule.view.EventItemView
import net.squanchy.schedule.view.EventViewHolder
import net.squanchy.search.SearchResult
import net.squanchy.search.domain.view.SearchListElement

internal class SearchAdapter(activity: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imageLoader: ImageLoader
    private val activity: Activity

    private lateinit var listener: SearchRecyclerView.OnSearchResultClickListener

    private var searchResult = SearchResult.Success(emptyList())

    init {
        this.activity = activity

        imageLoader = imageLoaderComponent(activity).imageLoader()
        setHasStableIds(true)
    }

    @ViewTypeId
    override fun getItemViewType(position: Int): Int {
        return when (searchResult.elements[position]) {
            is SearchListElement.EventHeader -> HEADER
            is SearchListElement.SpeakerHeader -> HEADER
            is SearchListElement.EventElement -> EVENT
            is SearchListElement.SpeakerElement -> SPEAKER
            is SearchListElement.AlgoliaLogo -> ALGOLIA_LOGO
        }
    }

    override fun getItemId(position: Int): Long {
        val item = searchResult.elements[position]
        return when (item) {
            is SearchListElement.EventHeader -> ITEM_ID_EVENTS_HEADER
            is SearchListElement.SpeakerHeader -> ITEM_ID_SPEAKERS_HEADER
            is SearchListElement.EventElement -> item.event.numericId
            is SearchListElement.SpeakerElement -> item.speaker.numericId
            is SearchListElement.AlgoliaLogo -> ITEM_ID_ALGOLIA_LOGO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, @ViewTypeId viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            HEADER -> return HeaderViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_search_header, parent, false))
            SPEAKER -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_search_result_small, parent, false)
                return SpeakerViewHolder(view)
            }
            EVENT -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_schedule_event_talk, parent, false)
                return EventViewHolder(view as EventItemView)
            }
            ALGOLIA_LOGO -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_search_algolia_logo, parent, false)
                return AlgoliaLogoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Item type $viewType not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = searchResult.elements[position]
        when (item) {
            is SearchListElement.EventHeader -> (holder as HeaderViewHolder).updateWith(HeaderType.EVENTS)
            is SearchListElement.SpeakerHeader -> (holder as HeaderViewHolder).updateWith(HeaderType.SPEAKERS)
            is SearchListElement.EventElement -> (holder as EventViewHolder).updateWith(item.event) { listener.onEventClicked(it) }
            is SearchListElement.SpeakerElement -> (holder as SpeakerViewHolder).updateWith(item.speaker, imageLoader, listener)
            is SearchListElement.AlgoliaLogo -> Unit // Nothing to do
        }
    }

    fun createSpanSizeLookup(columnsCount: Int): GridLayoutManager.SpanSizeLookup {
        return GridSpanSizeLookup(searchResult.elements, columnsCount)
    }

    override fun getItemCount(): Int = searchResult.elements.size

    fun updateWith(searchResult: SearchResult.Success, listener: SearchRecyclerView.OnSearchResultClickListener) {
        this.searchResult = searchResult
        this.listener = listener

        notifyDataSetChanged()
    }

    companion object {
        const val HEADER = 1
        const val SPEAKER = 2
        const val EVENT = 3
        const val ALGOLIA_LOGO = 4

        @IntDef(HEADER, SPEAKER, EVENT, ALGOLIA_LOGO)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ViewTypeId

        // These values are "random", we count on them not clashing with the other
        // random values that are used for non-hardcoded numeric IDs (for events
        // and speakers). This is a reasonable assumption in the Long range.
        // In addition, the CRC32 values we use as numeric IDs are always positive.
        private const val ITEM_ID_EVENTS_HEADER: Long = -100
        private const val ITEM_ID_SPEAKERS_HEADER: Long = -101
        private const val ITEM_ID_ALGOLIA_LOGO: Long = -102
    }
}
