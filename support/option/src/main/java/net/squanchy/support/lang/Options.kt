package net.squanchy.support.lang

import arrow.core.Option
import arrow.core.getOrElse

fun <T> Option<T>.or(value: T): T = this.getOrElse { value }

fun <T> Option<T>.getOrThrow(): T = this.getOrElse {
    throw IllegalStateException("You must check if data is present before using get()")
}

fun <T> T?.option(): Option<T> = Option.fromNullable(this)
