package net.squanchy.schedule.filterschedule

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View

class FilterScheduleDialog : DialogFragment() {

    private lateinit var service: FilterScheduleService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service = filterScheduleComponent(this).service()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
            .setTitle("Filter")
            .setNegativeButton("Cancel", { dialog, _ -> dialog.dismiss() })
            .setPositiveButton("Confirm", { _, _ -> onFilteringDone() })
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
