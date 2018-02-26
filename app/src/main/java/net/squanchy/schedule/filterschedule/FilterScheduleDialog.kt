package net.squanchy.schedule.filterschedule

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import net.squanchy.R
import net.squanchy.service.firestore.model.schedule.TrackService
import net.squanchy.schedule.domain.view.Track
import net.squanchy.service.repository.TrackFilter

class FilterScheduleDialog : DialogFragment() {

    private lateinit var trackService: TrackService
    private lateinit var trackFilter: TrackFilter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val component = trackFilterComponent(context!!)
        trackService = component.trackService()
        trackFilter = component.trackFilter()

        // TODO these should not be snapshots but be responsive! (the Dialog won't work for us)
        val tracks = tracksSnapshot()
        val selectedTracks = selectedTracksSnapshot()
        val trackSelectionStatuses = tracks.map { track -> selectedTracks.contains(track) }.toBooleanArray()

        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.filter_schedule)
            .setNeutralButton("done", { _, _ -> dismiss() })
            .setMultiChoiceItems(tracks.trackNames(), trackSelectionStatuses, { _, index, isChecked ->
                if (isChecked) selectedTracks.add(tracks[index]) else selectedTracks.remove(tracks[index])
                trackFilter.updateSelectedTracks(selectedTracks)
            })
            .create()
    }

    private fun selectedTracksSnapshot() = trackFilter.selectedTracks
        .blockingSingle()
        .toMutableSet()

    private fun tracksSnapshot() = trackService.tracks()
        .takeUntil { it.isNotEmpty() }
        .blockingSingle()

    private fun List<Track>.trackNames() = map { it.name }.toTypedArray()
}
