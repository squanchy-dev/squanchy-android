package net.squanchy.support.content.res

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
internal fun Resources.Theme.getColorFromAttribute(@AttrRes attributeId: Int): Int {
    val typedValue = TypedValue()
    resolveAttribute(attributeId, typedValue, true)
    return typedValue.data
}
