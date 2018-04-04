package net.squanchy.support.lang

interface Func0<out V> {

    fun call(): V
}

interface Func1<in T, out R> {

    fun call(t: T): R
}

// Just a convenience subclass of Func1
interface Predicate<in T> : Func1<T, Boolean>
