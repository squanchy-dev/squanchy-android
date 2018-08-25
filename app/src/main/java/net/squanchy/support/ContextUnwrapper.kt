@file:JvmName("ContextUnwrapper")

package net.squanchy.support

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

tailrec fun Context?.unwrapToActivityContext(): AppCompatActivity =
    when (this) {
        null -> throw IllegalArgumentException("Context must not be null")
        is AppCompatActivity -> this
        is ContextWrapper -> this.baseContext.unwrapToActivityContext()
        else -> throw IllegalArgumentException("Context type not supported: ${this.javaClass.canonicalName}")
    }
