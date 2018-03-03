package net.squanchy.schedule.tracksfilter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_track_filters.*
import net.squanchy.R
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.repository.TracksRepository

class ScheduleTracksFilterActivity : AppCompatActivity() {

    private lateinit var tracksRepository: TracksRepository
    private lateinit var tracksFilter: TracksFilter
    private lateinit var trackAdapter: TracksFilterAdapter

    private var subscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_track_filters)

        val component = tracksFilterComponent(this)
        tracksRepository = component.tracksRepository()
        tracksFilter = component.tracksFilter()

        trackAdapter = TracksFilterAdapter(this)

        trackFiltersList.layoutManager = LinearLayoutManager(this)
        trackFiltersList.adapter = trackAdapter
    }

    override fun onStart() {
        super.onStart()

        subscription = Observable.combineLatest(tracksRepository.tracks(), tracksFilter.selectedTracks, combineIntoCheckableTracks())
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { checkableTracks ->
                trackAdapter.updateTracks(checkableTracks) { track, selected ->
                    val selectedTracks = checkableTracks.allSelected()
                    val newSelectedTracks = selectedTracks.addOrRemove(track, selected)
                    tracksFilter.updateSelectedTracks(newSelectedTracks)
                    trackAdapter.notifyItemChanged(checkableTracks.map { checkableTrack -> checkableTrack.first }.indexOf(track))
                }
            }
    }

    private fun Iterable<CheckableTrack>.allSelected(): Set<Track> =
        filter { it.selected() }.map { it.track() }.toSet()

    private fun Set<Track>.addOrRemove(track: Track, selected: Boolean): Set<Track> =
        if (selected) this + track else this - track

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

private class TracksFilterAdapter(context: Context) : RecyclerView.Adapter<TrackViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val layoutInflater = LayoutInflater.from(context)

    private var checkableTracks: List<CheckableTrack> = emptyList()
    private lateinit var trackStateChangeListener: OnTrackSelectedChangeListener

    override fun getItemCount(): Int = checkableTracks.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TrackViewHolder {
        val view = layoutInflater.inflate(R.layout.track_filters_item, parent, false) as CheckBox
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(checkableTracks[position], trackStateChangeListener)
    }

    override fun getItemId(position: Int): Long {
        return checkableTracks[position].track().id.hashCode().toLong() // TODO this should use a proper checksum
    }

    fun updateTracks(newCheckableTracks: List<CheckableTrack>, listener: OnTrackSelectedChangeListener) {
        checkableTracks = newCheckableTracks // TODO use DiffUtil instead
        trackStateChangeListener = listener
        notifyDataSetChanged()
    }
}

private class TrackViewHolder(val item: CheckBox) : RecyclerView.ViewHolder(item) {

    fun bind(checkableTrack: CheckableTrack, listener: OnTrackSelectedChangeListener) {
        val (track, selected) = checkableTrack

        item.apply {
            text = track.name
            tag = track
            isChecked = selected

            if (track.accentColor.isPresent) {
                tintCheckbox(Color.parseColor(track.accentColor.get()))
            }

            setOnClickListener { listener.invoke(track, isChecked) }
        }
    }

    private fun CheckBox.tintCheckbox(@ColorInt color: Int) {
        val tintList = ColorStateList.valueOf(color)
        buttonTintList = tintList
    }
}

private typealias OnTrackSelectedChangeListener = (track: Track, selected: Boolean) -> Unit

private typealias CheckableTrack = Pair<Track, Boolean>

private fun CheckableTrack.track() = this.first
private fun CheckableTrack.selected() = this.second
