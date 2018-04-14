package net.squanchy.eventdetails.widget

import android.content.Context
import android.content.res.Resources
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
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

class EventDetailsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val dateTimeFormatter = DateTimeFormat.forPattern(WHEN_DATE_TIME_FORMAT)

    override fun onFinishInflate() {
        super.onFinishInflate()

        inflate(context, R.layout.merge_event_details_layout, this)
    }

    fun updateWith(event: Event) {
        updateWhen(event.startTime, event.timeZone)
        updateWhere(event.place)
        updateLevel(event.experienceLevel)
        updateDescription(event.description)
    }

    private fun updateWhen(startTime: LocalDateTime, timeZone: DateTimeZone) {
        val formatter = dateTimeFormatter.withZone(timeZone)
        whenValue.text = formatter.print(startTime.toDateTime(timeZone))
        whenGroup.isVisible = true
    }

    private fun updateWhere(place: Option<Place>) {
        if (place.isDefined()) {
            whereGroup.isVisible = true
            whereValue.text = place.getOrThrow().toPlaceLabel()
        } else {
            whereGroup.isVisible = false
        }
    }

    private fun Place.toPlaceLabel(): CharSequence {
        val builder = SpannableStringBuilder(name)
        if (floor.isDefined()) {
            val floorLabel = floor.getOrThrow()
            builder.append("   ")
                .append(floorLabel)
                .setSpan(
                        createColorSpan(whereValue, android.R.attr.textColorSecondary),
                        builder.length - floorLabel.length,
                        builder.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
        }
        return builder
    }

    private fun updateLevel(level: Option<ExperienceLevel>) {
        if (level.isDefined()) {
            levelGroup.isVisible = true

            val experienceLevel = level.getOrThrow()
            levelValue.setText(experienceLevel.labelStringResId)
            tintCompoundDrawableEnd(experienceLevel)
        } else {
            levelGroup.isVisible = false
        }
    }

    private fun tintCompoundDrawableEnd(experienceLevel: ExperienceLevel) {
        val compoundDrawables = levelValue.compoundDrawablesRelative
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
            descriptionGroup.isVisible = true
            descriptionValue.text = parseHtml(description.getOrThrow())
        } else {
            descriptionGroup.isVisible = false
        }
    }

    companion object {

        private const val WHEN_DATE_TIME_FORMAT = "EEEE, d MMMM 'at' HH:mm"
    }
}
