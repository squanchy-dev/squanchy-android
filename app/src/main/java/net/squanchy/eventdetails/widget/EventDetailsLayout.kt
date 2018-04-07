package net.squanchy.eventdetails.widget

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import arrow.core.Option
import kotlinx.android.synthetic.main.merge_event_details_layout.view.*
import net.squanchy.R
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.support.lang.getOrThrow
import org.joda.time.format.DateTimeFormat

// TODO flatten this layout as a ConstraintLayout
class EventDetailsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    init {
        super.setOrientation(LinearLayout.VERTICAL)
    }

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException("Changing orientation is not supported for EventDetailsLayout")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        View.inflate(context, R.layout.merge_event_details_layout, this)
    }

    fun updateWith(event: Event) {
        updateWhen(event)
        updateWhere(event)
        updateLevel(event.experienceLevel)
        updateDescription(event.description)
    }

    private fun updateWhen(event: Event) {
        val formatter = DateTimeFormat.forPattern(WHEN_DATE_TIME_FORMAT).withZone(event.timeZone)
        whenTextView.text = formatter.print(event.startTime.toDateTime())
        whenContainer.visibility = View.VISIBLE
    }

    private fun updateWhere(event: Event) {
        if (event.place.isDefined()) {
            whereContainer.visibility = View.VISIBLE
            whereTextView.text = placeTextFrom(event.place.getOrThrow())
        } else {
            whereContainer.visibility = View.GONE
        }
    }

    private fun placeTextFrom(place: Place): CharSequence {
        val builder = SpannableStringBuilder(place.name)
        if (place.floor.isDefined()) {
            val floorLabel = place.floor.getOrThrow()
            builder.append("   ")
                .append(floorLabel)
                .setSpan(
                        createColorSpan(whereTextView, android.R.attr.textColorSecondary),
                        builder.length - floorLabel.length,
                        builder.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
        }
        return builder
    }

    private fun updateLevel(level: Option<ExperienceLevel>) {
        if (level.isDefined()) {
            levelContainer.visibility = View.VISIBLE

            val experienceLevel = level.getOrThrow()
            levelTextView.setText(experienceLevel.labelStringResId)
            tintCompoundDrawableEnd(experienceLevel)
        } else {
            levelContainer.visibility = View.GONE
        }
    }

    private fun tintCompoundDrawableEnd(experienceLevel: ExperienceLevel) {
        val compoundDrawables = levelTextView.compoundDrawablesRelative
        val endCompoundDrawable = compoundDrawables[2]
        endCompoundDrawable?.setTint(ContextCompat.getColor(context, experienceLevel.colorResId))
    }

    private fun createColorSpan(targetView: View, @AttrRes attributeResId: Int): ForegroundColorSpan {
        val color = getColorFromTheme(targetView.context.theme, attributeResId)
        return ForegroundColorSpan(color)
    }

    @ColorInt
    private fun getColorFromTheme(theme: Resources.Theme, @AttrRes attributeId: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attributeId, typedValue, true)
        return typedValue.data
    }

    private fun updateDescription(description: Option<String>) {
        if (description.isDefined()) {
            descriptionHeader.visibility = View.VISIBLE
            descriptionTextView.visibility = View.VISIBLE
            descriptionTextView.text = parseHtml(description.getOrThrow())
        } else {
            descriptionHeader.visibility = View.GONE
            descriptionTextView.visibility = View.GONE
        }
    }

    @TargetApi(Build.VERSION_CODES.N) // The older fromHtml() is only called pre-24
    private fun parseHtml(description: String): Spanned {
        // TODO handle this properly
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION") // This is a "compat" method call, we only use this on pre-N
            Html.fromHtml(description)
        }
    }

    companion object {

        private const val WHEN_DATE_TIME_FORMAT = "EEEE, d MMMM 'at' HH:mm"
    }
}
