package net.squanchy.eventdetails.widget

import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import net.squanchy.R

import net.squanchy.eventdetails.domain.view.ExperienceLevel

class ExperienceLevelView : AppCompatTextView {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        setExperienceLevel(ExperienceLevel.BEGINNER)
    }

    fun setExperienceLevel(experienceLevel: ExperienceLevel) {
        return when (experienceLevel) {
            ExperienceLevel.BEGINNER -> updateValues(R.string.experience_level_beginner, R.color.experience_level_beginner)
            ExperienceLevel.INTERMEDIATE -> updateValues(R.string.experience_level_intermediate, R.color.experience_level_intermediate)
            ExperienceLevel.ADVANCED -> updateValues(R.string.experience_level_advanced, R.color.experience_level_advanced)
        }
    }

    private fun updateValues(@StringRes textStringResId: Int, @ColorRes backgroundColorResId: Int) {
        text = context.getString(textStringResId)
        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, backgroundColorResId))
    }
}
