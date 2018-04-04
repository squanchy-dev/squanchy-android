package net.squanchy.search.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWith(headerType: HeaderType) {
        (itemView as TextView).setText(headerType.headerTextResourceId())
    }

    fun updateWith(label: CharSequence) {
        (itemView as TextView).text = label
    }
}
