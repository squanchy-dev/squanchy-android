package net.squanchy.eventdetails.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
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
