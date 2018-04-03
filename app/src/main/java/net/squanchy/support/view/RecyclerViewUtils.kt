package net.squanchy.support.view

import android.support.v7.widget.RecyclerView

internal fun RecyclerView.setAdapterIfNone(newAdapter: RecyclerView.Adapter<*>) {
    if (adapter == null) adapter = newAdapter
}
