package net.squanchy.support.lang

import arrow.core.Option
import arrow.core.getOrElse

fun <T> Option<T>.or(value: T): T {
    return this.getOrElse { value }
}

fun <T> Option<T>.getOrThrow(): T {
    return this.getOrElse { throw IllegalStateException("You must check if data is present before using get()") }
}
