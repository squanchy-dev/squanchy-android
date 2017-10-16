@file:JvmName("TypefaceCompat")

package net.squanchy.support.font

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned


fun CharSequence.applyTypeface(typeface: Typeface) =
        SpannableString(this).applyTypeface(typeface)

fun Spannable.applyTypeface(typeface: Typeface) =
        apply { setSpan(CustomTypefaceSpan(typeface), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }

fun Spanned.hasTypefaceSpan() = getSpans(0, length, CustomTypefaceSpan::class.java).isNotEmpty()
