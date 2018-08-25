package net.squanchy.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import net.squanchy.R
import net.squanchy.schedule.domain.view.Event

internal class FavoritesAdapter(
    context: Context
) : ListAdapter<FavoritesItem, FavoritesViewHolder<*>>(DiffCallback) {

    companion object {
        private const val VIEW_TYPE_TALK: Int = 1
        private const val VIEW_TYPE_HEADER: Int = 2
    }

    private var favoriteClickListener: OnFavoriteClickListener? = null
    private var showRoom = false

    private val layoutInflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).id

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            FavoritesItem.Type.HEADER -> VIEW_TYPE_HEADER
            FavoritesItem.Type.FAVOURITE -> VIEW_TYPE_TALK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder<*> {
        val itemView = when (viewType) {
            VIEW_TYPE_TALK -> {
                layoutInflater.inflate(R.layout.item_schedule_event_talk, parent, false)
            }
            VIEW_TYPE_HEADER -> {
                layoutInflater.inflate(R.layout.item_search_header, parent, false)
            }
            else -> throw IllegalArgumentException("View type not supported: $viewType")
        }

        return favoriteItemViewHolderFor(itemView)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder<*>, position: Int) {
        when (holder) {
            is EventViewHolder -> holder.updateWith((getItem(position) as FavoritesItem.Favorite).event, showRoom, favoriteClickListener)
            is HeaderViewHolder -> holder.updateWith((getItem(position) as FavoritesItem.Header).date)
        }
    }

    fun updateWith(list: List<FavoritesItem>, showRoom: Boolean, listener: OnFavoriteClickListener) {
        this.showRoom = showRoom
        this.favoriteClickListener = listener
        super.submitList(list)
    }

    @Deprecated(
        message = "Use updateWith() instead",
        replaceWith = ReplaceWith("updateWith(list, showRoom, favoriteClickListener)"),
        level = DeprecationLevel.ERROR
    )
    override fun submitList(list: MutableList<FavoritesItem>?) {
        throw UnsupportedOperationException("Use updateWith() instead")
    }
}

private object DiffCallback : DiffUtil.ItemCallback<FavoritesItem>() {

    override fun areItemsTheSame(oldItem: FavoritesItem, newItem: FavoritesItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoritesItem, newItem: FavoritesItem): Boolean {
        return oldItem == newItem
    }
}

typealias OnFavoriteClickListener = (Event) -> Unit
