@file:JvmName("TypefaceCompat")

package net.squanchy.support.font

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

fun CharSequence.applyTypeface(typeface: Typeface) {
    if (this is Spannable)
        apply { setSpan(TypefaceSpan(typeface), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
    else
        SpannableString(this).applyTypeface(typeface)
}

fun Spanned.hasTypefaceSpan() = getSpans(0, length, TypefaceSpan::class.java).isNotEmpty()

private class TypefaceSpan(typeface: Typeface) : MetricAffectingSpan() {

    private val delegate = TypefaceDelegate(typeface)

    override fun updateMeasureState(p: TextPaint) {
        delegate.applyTypefaceTo(p)
    }

    override fun updateDrawState(tp: TextPaint) {
        delegate.applyTypefaceTo(tp)
    }
}
