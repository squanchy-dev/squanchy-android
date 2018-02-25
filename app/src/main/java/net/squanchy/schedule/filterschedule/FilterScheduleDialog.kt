package net.squanchy.schedule.filterschedule

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import net.squanchy.R

class FilterScheduleDialog : DialogFragment() {

    private lateinit var service: FilterScheduleService

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        service = filterScheduleComponent(this).service()

        val selection = if (savedInstanceState == null) {
            service.currentSelection
        } else {
            val restoredSelection = savedInstanceState.getBooleanArray(KEY_SELECTION)
            service.restore(restoredSelection)
            restoredSelection
        }

        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.filter_schedule)
            .setNegativeButton(android.R.string.no, { dialog, _ -> dialog.dismiss() })
            .setPositiveButton(android.R.string.yes, { _, _ -> onFilteringDone() })
            .setMultiChoiceItems(service.trackNames, selection, { _, which, isChecked ->
                if (isChecked) {
                    service.add(which)
                } else {
                    service.remove(which)
                }
            })
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBooleanArray(KEY_SELECTION, service.currentSelection)
    }

    private fun onFilteringDone() {
        service.confirm()
        dismiss()
    }

    companion object {
        private const val KEY_SELECTION = "KEY_SELECTION"
    }
}
