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

internal class SearchAdapter(activity: AppCompatActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imageLoader: ImageLoader
    private val activity: Activity

    private lateinit var listener: SearchRecyclerView.OnSearchResultClickListener

    private var searchResult = SearchResult.Success(emptyList(), emptyList())
    private var itemsAdapter = ItemsAdapter(searchResult)

    init {
        this.activity = activity

        imageLoader = imageLoaderComponent(activity).imageLoader()
        setHasStableIds(true)
    }

    @ViewTypeId
    override fun getItemViewType(position: Int): Int {
        return itemsAdapter.viewTypeAtAbsolutePosition(position)
    }

    override fun getItemId(position: Int): Long {
        return itemsAdapter.itemIdAtAbsolutePosition(position)
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
        val viewType = itemsAdapter.viewTypeAtAbsolutePosition(position)

        when (viewType) {
            SPEAKER -> (holder as SpeakerViewHolder).updateWith(itemsAdapter.speakerAtAbsolutePosition(position), imageLoader, listener)
            HEADER -> (holder as HeaderViewHolder).updateWith(itemsAdapter.headerTypeAtAbsolutePosition(position))
            EVENT -> (holder as EventViewHolder).updateWith(itemsAdapter.eventAtAbsolutePosition(position)) { listener.onEventClicked(it) }
            ALGOLIA_LOGO -> Unit // Nothing to do
            else -> throw IllegalArgumentException("Item type $viewType not supported")
        }
    }

    fun createSpanSizeLookup(columnsCount: Int): GridLayoutManager.SpanSizeLookup {
        return GridSpanSizeLookup(itemsAdapter, columnsCount)
    }

    override fun getItemCount(): Int {
        return itemsAdapter.totalItemsCount()
    }

    fun updateWith(searchResult: SearchResult.Success, listener: SearchRecyclerView.OnSearchResultClickListener) {
        this.searchResult = searchResult
        this.itemsAdapter = ItemsAdapter(searchResult)
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
    }
}
