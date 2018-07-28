package net.squanchy.speaker.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_speaker_details.view.*
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.text.parseHtml

class SpeakerDetailsLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    fun updateWith(speaker: Speaker) {
        speakerDetailsHeader.updateWith(speaker)
        speakerBio.text = speaker.bio.parseHtml()
    }
}
