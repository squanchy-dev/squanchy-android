package net.squanchy.support.lang

fun <T> T?.optional(): Optional<T> = Optional.fromNullable(this)
