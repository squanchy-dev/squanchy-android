package net.squanchy.schedule.tracksfilter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.postOnAnimationDelayed
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
import net.squanchy.service.repository.TracksRepository
import net.squanchy.support.view.setAdapterIfNone
import net.squanchy.support.widget.OriginCoordinates
import timber.log.Timber
import kotlin.math.hypot

class ScheduleTracksFilterActivity : AppCompatActivity() {

    private lateinit var tracksRepository: TracksRepository
    private lateinit var tracksFilter: TracksFilter
    private lateinit var trackAdapter: TracksFilterAdapter

    private lateinit var appearInterpolator: Interpolator

    private var subscription: Disposable? = null
    private var checkableTracks: List<CheckableTrack> = emptyList()

    private var needsAppearAnimation = true
    private var appearAnimationOrigin: OriginCoordinates? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_track_filters)

        savedInstanceState?.apply {
            needsAppearAnimation = getBoolean(EXTRA_NEEDS_APPEAR_ANIMATION, true)
            appearAnimationOrigin = getParcelable(EXTRA_APPEAR_ANIMATION_ORIGIN)
        }

        appearAnimationOrigin = appearAnimationOrigin ?: intent.getParcelableExtra(EXTRA_APPEAR_ANIMATION_ORIGIN)

        backgroundDim.setOnClickListener { closeFilters() }

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
        trackFiltersList.itemAnimator = null
    }

    private fun closeFilters() {
        if (appearAnimationOrigin == null) {
            finish()
        } else {
            animateDisappearance(appearAnimationOrigin!!)
        }
    }

    private fun animateDisappearance(origin: OriginCoordinates) {
        backgroundDim.isEnabled = false

        val (centerX, centerY) = origin

        ViewAnimationUtils.createCircularReveal(
            filtersRoot,
            centerX.toInt(),
            centerY.toInt(),
            hypot(filtersRoot.width.toDouble(), filtersRoot.height.toDouble()).toFloat(),
            0F
        ).apply {
            duration = resources.getInteger(R.integer.track_filters_disappear_duration).toLong()

            doOnEnd {
                filtersRoot.isInvisible = true
                finish()
            }
        }.start()
    }

    private fun Set<Track>.addOrRemove(track: Track, selected: Boolean): Set<Track> =
        if (selected) this + track else this - track

    override fun onStart() {
        super.onStart()

        if (needsAppearAnimation && appearAnimationOrigin != null) {
            needsAppearAnimation = false
            prepareAppearAnimation()
            filtersRoot.postOnAnimation { animateAppearing(appearAnimationOrigin!!) }
        }

        subscription = Observable.combineLatest(tracksRepository.tracks(), tracksFilter.selectedTracks, combineIntoCheckableTracks())
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { checkableTracks ->
                    trackFiltersList.setAdapterIfNone(trackAdapter)
                    this.checkableTracks = checkableTracks
                    trackAdapter.submitList(checkableTracks)
                },
                Timber::e
            )
    }

    private fun prepareAppearAnimation() {
        appearInterpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in)
        filtersRoot.isVisible = true

        val titleDeltaY = resources.getDimension(R.dimen.track_filters_title_appear_delta_y)
        dialogTitle.apply {
            translationY = -titleDeltaY
            alpha = 0F
        }

        val subtitleDeltaY = resources.getDimension(R.dimen.track_filters_subtitle_appear_delta_y)
        dialogSubtitle.apply {
            translationY = -subtitleDeltaY
            alpha = 0F
        }

        val tracksDeltaY = resources.getDimension(R.dimen.track_filters_tracks_appear_delta_y)
        trackFiltersList.apply {
            translationY = -tracksDeltaY
            alpha = 0F
        }
    }

    @Suppress("MagicNumber") // Just animation code… needs a few magic numbers
    private fun animateAppearing(origin: OriginCoordinates) {
        val (centerX, centerY) = origin

        ViewAnimationUtils.createCircularReveal(
            filtersRoot,
            centerX.toInt(),
            centerY.toInt(),
            0F,
            hypot(filtersRoot.width.toDouble(), filtersRoot.height.toDouble()).toFloat()
        ).apply {
            val totalDuration = resources.getInteger(R.integer.track_filters_appear_duration).toLong()
            val delay = resources.getInteger(R.integer.track_filters_appear_delay).toLong()

            duration = totalDuration

            filtersRoot.postOnAnimationDelayed(totalDuration / 2) {
                dialogTitle.slideDownAndFadeIn(duration = totalDuration - delay, delay = 0)
                dialogSubtitle.slideDownAndFadeIn(duration = totalDuration - 2 * delay, delay = delay)
                trackFiltersList.slideDownAndFadeIn(duration = totalDuration - 3 * delay, delay = 2 * delay)
            }
        }.start()
    }

    private fun View.slideDownAndFadeIn(duration: Long, delay: Long = 0) = this.animate()
        .translationY(0F)
        .alpha(1F)
        .setInterpolator(appearInterpolator)
        .setDuration(duration)
        .setStartDelay(delay)
        .start()

    private fun combineIntoCheckableTracks(): BiFunction<List<Track>, Set<Track>, List<CheckableTrack>> {
        return BiFunction { tracks, selectedTracks ->
            tracks.map { track -> CheckableTrack(track, selectedTracks.contains(track)) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(EXTRA_NEEDS_APPEAR_ANIMATION, needsAppearAnimation)
        outState?.putParcelable(EXTRA_APPEAR_ANIMATION_ORIGIN, appearAnimationOrigin)
    }

    override fun onStop() {
        super.onStop()
        subscription?.dispose()
    }

    companion object {
        private const val EXTRA_NEEDS_APPEAR_ANIMATION = "ScheduleTracksFilterActivity.EXTRA_NEEDS_APPEAR_ANIMATION"
        private const val EXTRA_APPEAR_ANIMATION_ORIGIN = "ScheduleTracksFilterActivity.EXTRA_APPEAR_ANIMATION_ORIGIN"

        fun createIntent(context: Context, originCoordinates: OriginCoordinates?): Intent {
            return Intent(context, ScheduleTracksFilterActivity::class.java).apply {
                putExtra(EXTRA_APPEAR_ANIMATION_ORIGIN, originCoordinates)
            }
        }
    }
}
