package net.squanchy.schedule.tracksfilter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_track_filters.*
import net.squanchy.R
import net.squanchy.schedule.domain.view.Track
import net.squanchy.schedule.tracksfilter.widget.FilterChipView
import net.squanchy.service.repository.TracksRepository

class ScheduleTracksFilterActivity : AppCompatActivity() {

    private lateinit var tracksRepository: TracksRepository
    private lateinit var tracksFilter: TracksFilter
    private lateinit var trackAdapter: TracksFilterAdapter

    private var subscription: Disposable? = null
    private var checkableTracks: List<CheckableTrack> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_track_filters)

        backgroundDim.setOnClickListener { finish() }
        closeButton.setOnClickListener { finish() }

        val component = tracksFilterComponent(this)
        tracksRepository = component.tracksRepository()
        tracksFilter = component.tracksFilter()

        trackAdapter = TracksFilterAdapter(this) { track, selected ->
            val selectedTracks = checkableTracks.allSelected()
            val newSelectedTracks = selectedTracks.addOrRemove(track, selected)
            tracksFilter.updateSelectedTracks(newSelectedTracks)
        }

        trackFiltersList.layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW)
        trackFiltersList.addItemDecoration(FlexboxItemDecoration(this).apply {
            setDrawable(resources.getDrawable(R.drawable.filters_separator, theme))
            setOrientation(FlexboxItemDecoration.BOTH)
        })
        trackFiltersList.adapter = trackAdapter
        trackFiltersList.itemAnimator = null
    }

    private fun Set<Track>.addOrRemove(track: Track, selected: Boolean): Set<Track> =
        if (selected) this + track else this - track

    override fun onStart() {
        super.onStart()

        subscription = Observable.combineLatest(tracksRepository.tracks(), tracksFilter.selectedTracks, combineIntoCheckableTracks())
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { checkableTracks ->
                this.checkableTracks = checkableTracks
                trackAdapter.submitList(checkableTracks)
            }
    }

    private fun combineIntoCheckableTracks(): BiFunction<List<Track>, Set<Track>, List<CheckableTrack>> {
        return BiFunction { tracks, selectedTracks ->
            tracks.map { track -> CheckableTrack(track, selectedTracks.contains(track)) }
        }
    }

    override fun onStop() {
        super.onStop()
        subscription?.dispose()
    }
}

private class TracksFilterAdapter(
    context: Context,
    private val trackStateChangeListener: OnTrackSelectedChangeListener
) : ListAdapter<CheckableTrack, TrackViewHolder>(DiffCallback()) {

    init {
        setHasStableIds(true)
    }

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = layoutInflater.inflate(R.layout.track_filters_item, parent, false) as FilterChipView
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position), trackStateChangeListener)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).track().id.hashCode().toLong() // TODO this should use a proper checksum
    }

    class DiffCallback : DiffUtil.ItemCallback<CheckableTrack>() {
        override fun areItemsTheSame(oldItem: CheckableTrack?, newItem: CheckableTrack?): Boolean {
            return oldItem?.track()?.id == newItem?.track()?.id
        }

        override fun areContentsTheSame(oldItem: CheckableTrack?, newItem: CheckableTrack?): Boolean {
            return oldItem == newItem
        }
    }
}

private class TrackViewHolder(val item: FilterChipView) : RecyclerView.ViewHolder(item) {

    fun bind(checkableTrack: CheckableTrack, listener: OnTrackSelectedChangeListener) {
        val (track, selected) = checkableTrack

        item.apply {
            text = track.name

            color = when {
                track.accentColor.isPresent -> Color.parseColor(track.accentColor.get())
                else -> Color.MAGENTA // TODO handle default color for tracks (maybe gray-ish?)
            }

            onCheckedChangeListener = { _, checked -> listener.invoke(track, checked) }
            isChecked = selected
        }
    }
}

private typealias OnTrackSelectedChangeListener = (track: Track, selected: Boolean) -> Unit

private typealias CheckableTrack = Pair<Track, Boolean>

private fun Iterable<CheckableTrack>.allSelected(): Set<Track> =
    filter { it.selected() }.map { it.track() }.toSet()

private fun CheckableTrack.track() = this.first
private fun CheckableTrack.selected() = this.second
