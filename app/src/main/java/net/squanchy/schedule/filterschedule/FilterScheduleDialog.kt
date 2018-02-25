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

        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.filter_schedule)
            .setNegativeButton(android.R.string.no, { dialog, _ -> dialog.dismiss() })
            .setPositiveButton(android.R.string.yes, { _, _ -> onFilteringDone() })
            .setMultiChoiceItems(service.trackNames, service.currentSelection, { _, which, isChecked ->
                if (isChecked) {
                    service.add(which)
                } else {
                    service.remove(which)
                }
            })
            .create()
    }

    private fun onFilteringDone() {
        service.confirm()
        dismiss()
    }
}
