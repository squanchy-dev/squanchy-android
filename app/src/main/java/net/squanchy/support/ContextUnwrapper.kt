@file:JvmName("ContextUnwrapper")
package net.squanchy.support

import android.content.Context
import android.content.ContextWrapper
import android.support.v7.app.AppCompatActivity

tailrec fun unwrapToActivityContext(context: Context?): AppCompatActivity =
    when (context) {
        null -> throw IllegalArgumentException("Context must not be null")
        is AppCompatActivity -> context
        is ContextWrapper -> unwrapToActivityContext(context.baseContext)
        else -> throw IllegalArgumentException("Context type not supported: ${context.javaClass.canonicalName}")
    }
