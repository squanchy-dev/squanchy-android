package net.squanchy.support.view

import androidx.recyclerview.widget.RecyclerView

internal fun RecyclerView.setAdapterIfNone(newAdapter: RecyclerView.Adapter<*>) {
    if (adapter == null) adapter = newAdapter
}
