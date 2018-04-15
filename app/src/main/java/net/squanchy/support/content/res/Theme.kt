package net.squanchy.support.content.res

import android.content.res.Resources
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.util.TypedValue

@ColorInt
internal fun Resources.Theme.getColorFromAttribute(@AttrRes attributeId: Int): Int {
    val typedValue = TypedValue()
    resolveAttribute(attributeId, typedValue, true)
    return typedValue.data
}
