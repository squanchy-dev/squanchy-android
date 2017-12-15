package net.squanchy.injection

import android.content.Context

import net.squanchy.SquanchyApplication

val Context.applicationComponent: ApplicationComponent
    get() = (applicationContext as SquanchyApplication).applicationComponent
