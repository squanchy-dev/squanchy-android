package net.squanchy.speaker.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_speaker_details.view.*

import net.squanchy.R
import net.squanchy.speaker.domain.view.Speaker

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

    @TargetApi(Build.VERSION_CODES.N)
    // The older fromHtml() is only called pre-24
    private fun parseHtml(description: String): Spanned {
        // TODO handle this properly
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(description)
        }
    }
}
