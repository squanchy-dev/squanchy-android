package net.squanchy.speaker.widget

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import kotlinx.android.synthetic.main.activity_speaker_details.view.*
import net.squanchy.speaker.domain.view.Speaker
import net.squanchy.support.text.parseHtml

class SpeakerDetailsLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    fun updateWith(speaker: Speaker) {
        speakerDetailsHeader.updateWith(speaker)
        speakerBio.text = parseHtml(speaker.bio)
    }
}
