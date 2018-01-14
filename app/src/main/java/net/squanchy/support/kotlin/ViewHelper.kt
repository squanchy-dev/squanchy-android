package net.squanchy.support.kotlin

import android.view.View
import android.view.ViewGroup

operator fun ViewGroup.get(index: Int) = getChildAt(index)

val ViewGroup.children: List<View>
    get() = (0 until childCount).map { getChildAt(it) }
