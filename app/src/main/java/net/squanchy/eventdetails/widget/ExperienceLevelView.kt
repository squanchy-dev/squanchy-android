package net.squanchy.eventdetails.widget

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import net.squanchy.eventdetails.domain.view.ExperienceLevel
import java.util.Locale

class ExperienceLevelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        setExperienceLevel(ExperienceLevel.BEGINNER)
    }

    fun setExperienceLevel(experienceLevel: ExperienceLevel) {
        text = context.getString(experienceLevel.labelStringResId).toLowerCase(Locale.getDefault())
        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, experienceLevel.colorResId))
    }
}
