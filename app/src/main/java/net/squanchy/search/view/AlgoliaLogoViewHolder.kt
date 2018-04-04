package net.squanchy.search.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import net.squanchy.R

class AlgoliaLogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        (itemView as ImageView).setImageResource(R.drawable.ic_algolia_logo)
    }
}
