package net.squanchy.speaker.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_speaker_details.view.*
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.text.parseHtml

class SpeakerDetailsLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        super.setOrientation(LinearLayout.VERTICAL)
    }

    override fun setOrientation(orientation: Int): Nothing {
        throw UnsupportedOperationException("Changing orientation is not supported for SpeakerDetailsLayout")
    }

    fun updateWith(speaker: Speaker) {
        speakerDetailsHeader.updateWith(speaker)
        speakerBio.text = parseHtml(speaker.bio)
    }
}
