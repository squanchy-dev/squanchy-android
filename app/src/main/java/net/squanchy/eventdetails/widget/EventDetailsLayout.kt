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
import android.widget.TextView
import net.squanchy.R
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import net.squanchy.schedule.domain.view.Event
import net.squanchy.schedule.domain.view.Place
import net.squanchy.support.lang.Optional
import org.joda.time.format.DateTimeFormat

class EventDetailsLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private lateinit var whenContainer: View
    private lateinit var whenTextView: TextView
    private lateinit var whereContainer: View
    private lateinit var whereTextView: TextView
    private lateinit var levelContainer: View
    private lateinit var levelTextView: TextView
    private lateinit var descriptionHeader: View
    private lateinit var descriptionTextView: TextView

    init {
        super.setOrientation(LinearLayout.VERTICAL)
    }

    override fun setOrientation(orientation: Int) {
        throw UnsupportedOperationException("Changing orientation is not supported for EventDetailsLayout")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        View.inflate(context, R.layout.merge_event_details_layout, this)

        whenTextView = findViewById(R.id.when_text)
        whenContainer = findViewById(R.id.when_container)
        whereTextView = findViewById(R.id.where_text)
        whereContainer = findViewById(R.id.where_container)
        levelTextView = findViewById(R.id.level_text)
        levelContainer = findViewById(R.id.level_container)
        descriptionHeader = findViewById(R.id.description_header)
        descriptionTextView = findViewById(R.id.description_text)
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
        if (event.place.isPresent) {
            whereContainer.visibility = View.VISIBLE
            whereTextView.text = placeTextFrom(event.place.get())
        } else {
            whereContainer.visibility = View.GONE
        }
    }

    private fun placeTextFrom(place: Place): CharSequence {
        val builder = SpannableStringBuilder(place.name)
        if (place.floor.isPresent) {
            val floorLabel = place.floor.get()
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

    private fun updateLevel(level: Optional<ExperienceLevel>) {
        if (level.isPresent) {
            levelContainer.visibility = View.VISIBLE

            val experienceLevel = level.get()
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

    private fun updateDescription(description: Optional<String>) {
        if (description.isPresent) {
            descriptionHeader.visibility = View.VISIBLE
            descriptionTextView.visibility = View.VISIBLE
            descriptionTextView.text = parseHtml(description.get())
        } else {
            descriptionHeader.visibility = View.GONE
            descriptionTextView.visibility = View.GONE
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private// The older fromHtml() is only called pre-24
    fun parseHtml(description: String): Spanned {
        // TODO handle this properly
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(description)
        }
    }

    companion object {

        private const val WHEN_DATE_TIME_FORMAT = "EEEE, d MMMM 'at' HH:mm"
    }
}
