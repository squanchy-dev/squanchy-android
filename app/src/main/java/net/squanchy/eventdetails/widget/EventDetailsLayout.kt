package net.squanchy.eventdetails.widget

import android.content.Context
import android.content.res.Resources
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.view.isVisible
import arrow.core.Option
import kotlinx.android.synthetic.main.merge_event_details_layout.view.*
import net.squanchy.R
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.support.lang.getOrThrow
import net.squanchy.support.text.parseHtml
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
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

    private val dateTimeFormatter = DateTimeFormat.forPattern(WHEN_DATE_TIME_FORMAT)

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException("Changing orientation is not supported for EventDetailsLayout")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        View.inflate(context, R.layout.merge_event_details_layout, this)
    }

    fun updateWith(event: Event) {
        updateWhen(event.startTime, event.timeZone)
        updateWhere(event.place)
        updateLevel(event.experienceLevel)
        updateDescription(event.description)
    }

    private fun updateWhen(startTime: LocalDateTime, timeZone: DateTimeZone) {
        val formatter = dateTimeFormatter.withZone(timeZone)
        whenTextView.text = formatter.print(startTime.toDateTime(timeZone))
        whenContainer.isVisible = true
    }

    private fun updateWhere(place: Option<Place>) {
        if (place.isDefined()) {
            whereContainer.isVisible = true
            whereTextView.text = place.getOrThrow().toPlaceLabel()
        } else {
            whereContainer.isVisible = false
        }
    }

    private fun Place.toPlaceLabel(): CharSequence {
        val builder = SpannableStringBuilder(name)
        if (floor.isDefined()) {
            val floorLabel = floor.getOrThrow()
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
            levelContainer.isVisible = true

            val experienceLevel = level.getOrThrow()
            levelTextView.setText(experienceLevel.labelStringResId)
            tintCompoundDrawableEnd(experienceLevel)
        } else {
            levelContainer.isVisible = false
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
            descriptionHeader.isVisible = true
            descriptionTextView.isVisible = true
            descriptionTextView.text = parseHtml(description.getOrThrow())
        } else {
            descriptionHeader.isVisible = false
            descriptionTextView.isVisible = false
        }
    }

    companion object {

        private const val WHEN_DATE_TIME_FORMAT = "EEEE, d MMMM 'at' HH:mm"
    }
}
