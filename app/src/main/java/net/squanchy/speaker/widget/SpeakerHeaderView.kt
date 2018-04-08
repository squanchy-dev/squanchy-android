package net.squanchy.speaker.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import arrow.core.Option
import kotlinx.android.synthetic.main.activity_speaker_details.view.*
import net.squanchy.R
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.imageLoaderComponent
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.lang.getOrThrow
import net.squanchy.support.unwrapToActivityContext

class SpeakerHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var imageLoader: ImageLoader? = null

    init {
        if (!isInEditMode) {
            val activity = unwrapToActivityContext(context)
            imageLoader = imageLoaderComponent(activity).imageLoader()
        }

        super.setOrientation(LinearLayout.HORIZONTAL)
    }

    override fun setOrientation(orientation: Int): Nothing {
        throw UnsupportedOperationException("Changing orientation is not supported for SpeakerHeaderView")
    }

    fun updateWith(speaker: Speaker) {
        updatePhoto(speaker.photoUrl, imageLoader)

        speakerName.text = speaker.name

        val companyName = speaker.companyName
        if (companyName.isDefined()) {
            // TODO support navigating to company website
            speakerCompany.text = companyName.getOrThrow()
            speakerCompany.visibility = View.VISIBLE
        } else {
            speakerCompany.visibility = View.GONE
        }
    }

    private fun updatePhoto(photoUrl: Option<String>, imageLoader: ImageLoader?) {
        if (imageLoader == null) {
            throw IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet")
        }

        if (photoUrl.isDefined()) {
            speakerPhoto.visibility = View.VISIBLE
            imageLoader.load(photoUrl.getOrThrow())
                .error(R.drawable.ic_no_avatar)
                .into(speakerPhoto)
        } else {
            speakerPhoto.setImageResource(R.drawable.ic_no_avatar)
        }
    }
}
