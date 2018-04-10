package net.squanchy.search

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_search_result_small.view.*
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.search.view.SearchRecyclerView.OnSearchResultClickListener
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.getOrThrow

class SearchItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        super.setOrientation(LinearLayout.VERTICAL)
    }

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException("SearchItem doesn't support changing orientation")
    }

    fun updateWith(speaker: Speaker, imageLoader: ImageLoader, listener: OnSearchResultClickListener) {
        speakerName.text = speaker.name
        updateSpeakerPhotos(speaker, imageLoader)
        setOnClickListener { listener.onSpeakerClicked(speaker) }
    }

    private fun updateSpeakerPhotos(speaker: Speaker, imageLoader: ImageLoader?) {
        if (imageLoader == null) {
            throw IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet")
        }

        val avatarImageURL = speaker.photoUrl
        if (avatarImageURL.isDefined()) {
            imageLoader.load(avatarImageURL.getOrThrow())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_no_avatar)
                .into(speakerPhoto)
        } else {
            speakerPhoto.setImageResource(R.drawable.ic_no_avatar)
        }
    }
}
