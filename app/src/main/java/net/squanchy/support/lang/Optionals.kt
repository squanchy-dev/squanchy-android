package net.squanchy.support.lang

fun <T> T?.asOptional(): Optional<T> = Optional.fromNullable(this)
