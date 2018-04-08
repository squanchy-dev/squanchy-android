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
import net.squanchy.search.SearchResult
import net.squanchy.search.SearchListElement
import net.squanchy.search.SearchListElement.EventElement
import net.squanchy.search.SearchListElement.SpeakerElement
import net.squanchy.search.view.SearchItemViewHolder.AlgoliaLogoViewHolder
import net.squanchy.search.view.SearchItemViewHolder.HeaderViewHolder
import net.squanchy.search.view.SearchItemViewHolder.SearchEventViewHolder
import net.squanchy.search.view.SearchItemViewHolder.SpeakerViewHolder

internal class SearchAdapter(activity: AppCompatActivity) : RecyclerView.Adapter<SearchItemViewHolder>() {

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
            is EventElement -> EVENT
            is SpeakerElement -> SPEAKER
            is SearchListElement.AlgoliaLogo -> ALGOLIA_LOGO
        }
    }

    override fun getItemId(position: Int): Long {
        val item = searchResult.elements[position]
        return when (item) {
            is EventElement -> item.event.numericId
            is SpeakerElement -> item.speaker.numericId
            is SearchListElement.EventHeader -> ITEM_ID_EVENTS_HEADER
            is SearchListElement.SpeakerHeader -> ITEM_ID_SPEAKERS_HEADER
            is SearchListElement.AlgoliaLogo -> ITEM_ID_ALGOLIA_LOGO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, @ViewTypeId viewType: Int): SearchItemViewHolder {
        when (viewType) {
            HEADER -> return HeaderViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_search_header, parent, false))
            SPEAKER -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_search_result_small, parent, false)
                return SpeakerViewHolder(view)
            }
            EVENT -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_schedule_event_talk, parent, false)
                return SearchEventViewHolder(view as EventItemView)
            }
            ALGOLIA_LOGO -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.item_search_algolia_logo, parent, false)
                return AlgoliaLogoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Item type $viewType not supported")
        }
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val item = searchResult.elements[position]
        when (item) {
            is SearchListElement.EventHeader -> (holder as HeaderViewHolder).updateWith(HeaderType.EVENTS)
            is SearchListElement.SpeakerHeader -> (holder as HeaderViewHolder).updateWith(HeaderType.SPEAKERS)
            is EventElement -> (holder as SearchEventViewHolder).updateWith(item.event) { listener.onEventClicked(it) }
            is SpeakerElement -> (holder as SpeakerViewHolder).updateWith(item.speaker, imageLoader, listener)
            is SearchListElement.AlgoliaLogo -> Unit // Nothing to do
        }
    }

    override fun getItemCount(): Int = searchResult.elements.size

    fun createSpanSizeLookup(columnsCount: Int): GridLayoutManager.SpanSizeLookup {
        return GridSpanSizeLookup(searchResult.elements, columnsCount)
    }

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

        // These values have been calculated manually using crc32, and they are never going to overlap
        // with the ones from speakers, events or tracks.
        private const val ITEM_ID_EVENTS_HEADER: Long = 3128461027
        private const val ITEM_ID_SPEAKERS_HEADER: Long = 1574748858
        private const val ITEM_ID_ALGOLIA_LOGO: Long = 3230264564
    }
}
