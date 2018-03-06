package net.squanchy.schedule.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView

import net.squanchy.R
import net.squanchy.support.widget.SpeakerView

class ScheduleSpeakerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SpeakerView(context, attrs, defStyleAttr, defStyleRes) {

    override fun inflatePhotoView(speakerPhotoContainer: ViewGroup): ImageView =
        layoutInflater.inflate(R.layout.view_speaker_photo_schedule, speakerPhotoContainer, false) as ImageView
}
