@file:JvmName("TypefaceCompat")

package net.squanchy.support.font

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

fun CharSequence.applyTypeface(typeface: Typeface): Spanned =
        (this as? Spannable)?.apply {
            setSpan(TypefaceSpan(typeface), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } ?: SpannableString(this).applyTypeface(typeface)

fun Spanned.hasTypefaceSpan() = getSpans(0, length, TypefaceSpan::class.java).isNotEmpty()

private class TypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {

    override fun updateMeasureState(p: TextPaint) {
        p.typeface = typeface
    }

    override fun updateDrawState(tp: TextPaint) {
        tp.typeface = typeface
    }
}
